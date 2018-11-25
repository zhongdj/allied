package hr.zhaopin.company.detail

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.MediaRanges._
import akka.http.scaladsl.model.MediaType.NotCompressible
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import reactivemongo.akkastream.AkkaStreamCursor
import reactivemongo.api.{Cursor, MongoConnection}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONArray, BSONDocument, BSONObjectID}
// Reactive streams imports
import org.reactivestreams.Publisher
import akka.stream.scaladsl.Source
import reactivemongo.akkastream.{AkkaStreamCursor, cursorProducer, State}

import scala.concurrent.Future
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

object DetailSpider extends App {

  import ExecutionContext.Implicits.global // use any appropriate context
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  val driver = new reactivemongo.api.MongoDriver
  val connection: MongoConnection = driver.connection(List("localhost:27017"))
  val coll: Future[BSONCollection] = connection.database("company").map(_.collection("company"))
  val detailcoll: Future[BSONCollection] = connection.database("company").map(_.collection("company_detail"))

  def eventualCompanies(): Future[Source[BSONDocument, Future[State]]] = coll.map { bColl =>
    companies(bColl)._1
  }

  def companies(collection: BSONCollection): (Source[BSONDocument, Future[State]], Publisher[BSONDocument]) = {
    val cursor: AkkaStreamCursor[BSONDocument] =
      collection.find(BSONDocument(
        "detailed_downloaded" -> BSONDocument("$exists" -> false))
      ).
        sort(BSONDocument("id" -> 1))
        .cursor[BSONDocument]()

    val src: Source[BSONDocument, Future[State]] = cursor.documentSource()
    val pub: Publisher[BSONDocument] = cursor.documentPublisher()

    src -> pub
  }

  val parallsm = 4

  def writeToMongo: (BSONObjectID, String) => Future[WriteResult] = (id, content) => detailcoll.flatMap { bColl =>
    bColl.insert(false)
      .one(BSONDocument(
        "pageId" -> id,
        "content" -> content
      )).flatMap { _ =>
      coll.flatMap { dColl =>
        dColl.update(false).one(
          BSONDocument("_id" -> id),
          BSONDocument("$set" -> BSONDocument("detailed_downloaded" -> true)), false, false)
      }
    }
  }

  eventualCompanies().map { source =>
    source.map(d => (d.getAs[BSONObjectID]("_id").get, d.getAs[String]("site")))
      .filter(_._2.nonEmpty)
      .map(p => (p._1, p._2.get))
      .map { p =>
        println(p)
        p
      }
      .mapAsync(parallsm)(eventualResponse.tupled)
      .mapAsync(4)(stringfyResponse.tupled)
      .mapAsync(10)(writeToMongo.tupled)
      .runWith(Sink.ignore)

  }

  private def stringfyResponse: (BSONObjectID, HttpResponse) => Future[(BSONObjectID, String)] = (id, response) => {
    response.entity.dataBytes.fold("")(_ ++ _.utf8String).runReduce[String](_ + _)
      .map((id, _))
  }

  private def eventualResponse: (BSONObjectID, String) => Future[(BSONObjectID, HttpResponse)] = (id, url) =>
    Http().singleRequest(createRequest(url)).map((id, _))

  private def createRequest(url: String) = {
    HttpRequest(uri = url, headers = List(
      `User-Agent`("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36"),
      Accept(MediaType.applicationBinary("json", NotCompressible), `*/*;q=MIN`),
      Connection("keep-alive"),
      Cookie(
        "GUID" -> "caabe777991041a5b6deed29ed5496ce",
        "HMACCOUNT" -> "13649213251EC96D",
        "Hm_lpvt_38ba284938d5eddca645bb5e02a02006" -> "1543157228",
        "Hm_lvt_38ba284938d5eddca645bb5e02a02006" -> "1543154912",
        "LastCity" -> "%E5%8C%97%E4%BA%AC",
        "LastCity%5Fid" -> "530",
        "SERVERID" -> "0db2f4553e294c56acd6f150d2fdf2b0|1543157228|1543157227",
        "ZL_REPORT_GLOBAL" -> "{%22sou%22:{%22actionid%22:%22bc4d665d-d89d-40e6-bd10-bab6a6f6d192-sou%22%2C%22funczone%22:%22smart_matching%22}}",
        "ZP_OLD_FLAG" -> "false",
        "acw_tc" -> "65c86a0b15431572267416882e80b39f4f2c5fe910b27601af6136e7ed355b",
        "amap_ver" -> "1536672475634",
        "guid" -> "f2f6-7cff-f497-157b",
        "jobRiskWarning" -> "true",
        "key" -> "6a7665aa7301eae686d9e79884d0445b",
        "sajssdk_2015_cross_new_user" -> "1",
        "sensorsdata2015jssdkcross" -> "%7B%22distinct_id%22%3A%221674b56a197b3b-021b5e14fe1b79-35637400-8294400-1674b56a198382%22%2C%22%24device_id%22%3A%221674b56a197b3b-021b5e14fe1b79-35637400-8294400-1674b56a198382%22%2C%22props%22%3A%7B%7D%7D",
        "sts_chnlsid" -> "Unknown",
        "sts_deviceid" -> "1674b569e8c1d0-035223d07d65e-35637400-8294400-1674b569e8e530",
        "sts_evtseq" -> "2",
        "sts_sg" -> "1",
        "sts_sid" -> "1674b56a0fe280-06e0172565266c-35637400-8294400-1674b56a0ff32d",
        "zp_src_url" -> "https%3A%2F%2Fwww.zhaopin.com%2F"
      ),
      Origin("https://sou.zhaopin.com"),
      Referer(uri = "https://sou.zhaopin.com/?jl=530&kw=Java%E5%BC%80%E5%8F%91&kt=3")
    ))
  }
}
