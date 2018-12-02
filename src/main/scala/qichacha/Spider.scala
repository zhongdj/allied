package qichacha

import java.net.URLEncoder
// Reactive streams imports
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import io.happy.StateBuilder._
import org.reactivestreams.Publisher
import reactivemongo.akkastream.{AkkaStreamCursor, State, cursorProducer}
import reactivemongo.api.MongoConnection
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONObjectID}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

object Spider extends App {

  val started = """.*?<table class="m_srchList">.*?<tr>.*?<img src="(.+?)".*?<a .*?href="(.+?)".*?<em><em>(.+?)</em></em>.*""".r

  val fsm = initialize[(String, String, String)] {
    case (r, line) if line contains ("家符合条件的企业") => r
  }.andThen {
    case (_, started(img, link, name)) => (name, link, img)
  }.build(("", "", ""))


  import ExecutionContext.Implicits.global // use any appropriate context
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  val driver = new reactivemongo.api.MongoDriver
  val connection: MongoConnection = driver.connection(List("localhost:27017"))
  val coll: Future[BSONCollection] = connection.database("company").map(_.collection("company_norm_active"))
  val detailcoll: Future[BSONCollection] = connection.database("company").map(_.collection("qichacha_detail"))

  def eventualCompanies(): Future[Source[BSONDocument, Future[State]]] = coll.map { bColl =>
    companies(bColl)._1
  }

  def companies(collection: BSONCollection): (Source[BSONDocument, Future[State]], Publisher[BSONDocument]) = {
    val cursor: AkkaStreamCursor[BSONDocument] =
      collection.find(BSONDocument(
        "url" -> BSONDocument("$exists" -> false))
      ).cursor[BSONDocument]()

    val src: Source[BSONDocument, Future[State]] = cursor.documentSource()
    val pub: Publisher[BSONDocument] = cursor.documentPublisher()

    src -> pub
  }

  def parseToLink: (BSONObjectID, String) => (BSONObjectID, String, String) = (id, lines) => {
    val extracted = lines.split("""\n""").foldLeft(fsm)(_ read _).extracted
    (id, extracted.get._2, extracted.get._3)
  }

  val parallelism = 1
  eventualCompanies().map { source =>
    source
      .map(parseTitle)
      .filter(_._2.nonEmpty)
      .map(p => (p._1, p._2.get))
      .map { p =>
        println(p)
        p
      }
      .throttle(1, 5 second)
      .mapAsync(parallelism)(eventualResponse.tupled)
      .mapAsync(parallelism)(stringfyResponse.tupled)
      .map(parseToLink.tupled)
      .mapAsync(parallelism)(writeUrlToMongo.tupled)
      .runWith(Sink.ignore)

  }

  private def parseTitle: BSONDocument => (BSONObjectID, Option[String]) = d => {
    (d.getAs[BSONObjectID]("_id").get, d.getAs[String]("title"))
  }

  def urlOf(companyName: String) = s"https://www.qichacha.com/search?key=${URLEncoder.encode(companyName)}"

  private def eventualResponse: (BSONObjectID, String) => Future[(BSONObjectID, HttpResponse)] = (id, companyName) =>
    Http().singleRequest(createRequest(urlOf(companyName))).map((id, _))

  private def createRequest(url: String) = {
    println(url)
    val r = HttpRequest(uri = url, headers = List(
      `User-Agent`("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36")
      //      custom("accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"),
      //      custom(":authority" -> "www.qichacha.com"),
      //      custom(":method:" -> "GET"),
      //      custom(":path" -> url.substring(url.lastIndexOf("/"))),
      //      custom(":scheme" -> "https"),
      //      custom("accept-encoding" -> "gzip, deflate, br"),
      //      custom("accept-language" -> "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7"),
      //      Cookie(
      //        "QCCSESSID" -> "cqnbvr8t4hu5nlqnb02t9kb2h5",
      //        "UM_distinctid" -> "1676a3f56934f5-0d3a513dabe8b4-35627600-1fa400-1676a3f5694438",
      //        "zg_did" -> "%7B%22did%22%3A%20%221676a3f5ab84c6-0783c60b9a3922-35627600-1fa400-1676a3f5ab98d8%22%7D",
      //        "Hm_lvt_3456bee468c83cc63fb5147f119f1075" -> "1543675798",
      //        "hasShow" -> "1",
      //        "_uab_collina" -> "154367579872550815854844",
      //        "saveFpTip" -> "true",
      //        "acw_tc" -> "ca6cfacc15436758016125716e5a519c327ad71a902114b519fda3cc79",
      //        "CNZZDATA1254842228" -> "1825209952-1543670836-%7C1543676236",
      //        "zg_de1d1a35bfa24ce29bbf2c7eb17e6c4f" -> "%7B%22sid%22%3A%201543675796155%2C%22updated%22%3A%201543677215521%2C%22info%22%3A%201543675796158%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22%22%7D",
      //        "Hm_lpvt_3456bee468c83cc63fb5147f119f1075" -> "1543677216"
      //      ),
      //      custom("upgrade-insecure-requests" -> "1"),
      //      Referer(uri = "https://www.qichacha.com/firm_10070efcf617c1ce03a8779911a7022a.html")
    ))
    println(r)
    r
  }

  def writeUrlToMongo: (BSONObjectID, String, String) => Future[WriteResult] = (id, url, img) => detailcoll.flatMap { bColl =>
    bColl.insert(false)
      .one(BSONDocument(
        "pageId" -> id,
        "url" -> url,
        "img" -> img
      )).flatMap { _ =>
      coll.flatMap { dColl =>
        dColl.update(false).one(
          BSONDocument("_id" -> id),
          BSONDocument("$set" -> BSONDocument("url" -> true)), false, false)
      }
    }
  }

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

  def custom(kv: (String, String)) = new CustomHeader {

    override def value(): String = kv._2

    override def renderInRequests(): Boolean = true

    override def renderInResponses(): Boolean = false

    override def name(): String = kv._1
  }

  private def stringfyResponse: (BSONObjectID, HttpResponse) => Future[(BSONObjectID, String)] = (id, response) => {
    val r = response.entity.dataBytes.fold("")(_ ++ _.utf8String).runReduce[String](_ + _)
      .map((id, _))
    r
  }
}
