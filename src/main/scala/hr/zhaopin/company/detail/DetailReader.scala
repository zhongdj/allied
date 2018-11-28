package hr.zhaopin.company.detail

import akka.stream.scaladsl.{Sink, Source}
import io.happy.MongoConnected
import io.happy.StateBuilder.initialize
import org.reactivestreams.Publisher
import play.api.libs.json.{Json, _}
import reactivemongo.akkastream.{AkkaStreamCursor, State, cursorProducer}
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future


object DetailReader extends App with MongoConnected {

  import scala.concurrent.ExecutionContext.Implicits.global

  val detailcoll: Future[JSONCollection] = connection.database("company").map(_.collection("company_detail"))
  val detailcoll2: Future[JSONCollection] = connection.database("company").map(_.collection("company_more"))

  def companies(collection: JSONCollection): (Source[JsObject, Future[State]], Publisher[JsObject]) = {
    val cursor: AkkaStreamCursor[JsObject] =
      collection.find(JsObject.empty)
        .cursor[JsObject]()

    val src: Source[JsObject, Future[State]] = cursor.documentSource()
    val pub: Publisher[JsObject] = cursor.documentPublisher()

    src -> pub
  }

  val json =
    """<script>__INITIAL_STATE__=(.+?)\</script>.*""".r


  type ResultType = JsObject // any type which is provided a `Writes[T]`

  val init = initialize[String] {
    case (_, line) if line.startsWith("<script>") => ""
  }.andThen {
    case (_, json(jsonText)) => jsonText
  }.build("")


  def export() = {
    detailcoll.map(companies).map(_._1).map { source =>
      source
        .map(d => d("content").validate[String].getOrElse(""))
        .filter(_.nonEmpty)
        .map { t =>
          t.split("""\n""").toList.
            foldLeft(init)(_ read _)
            .extracted
            .getOrElse(t)
        }.filter(_.nonEmpty)
        .map { jsonText =>
          Json.parse(jsonText).validate[JsObject].recover { case e: JsError => println(e)
            println("-------------------------------------------------------------------")
            println(jsonText)
            println("-------------------------------------------------------------------")
            JsObject.empty
          }.get
        }
        .mapAsync(1) { jsObj =>
          detailcoll2.map(_.insert[JsObject](false).one(jsObj("company").as[JsObject]))

        }
        .runWith(Sink.ignore)
    }
  }

  def export2() = {
    detailcoll.map(companies).map(_._1).map { source =>
      source
        .map(d => (d("_id"), d("content").validate[String].getOrElse("")))
        .filter(_._2.nonEmpty)
        .filter(t => Special.wids.contains(t._1("$oid").as[JsString].value))
        .map { t =>
          t._2.split("""\n""").toList.
            foldLeft(init)(_ read _)
            .extracted
            .getOrElse("")
        }.filter(_.nonEmpty)
        .map { jsonText =>
          Json.parse(jsonText).validate[JsObject].recover { case e: JsError => println(e)
            println("-------------------------------------------------------------------")
            println(jsonText)
            println("-------------------------------------------------------------------")
            JsObject.empty
          }.get
        }
        .mapAsync(1) { jsObj =>
          detailcoll2.map(_.insert[JsObject](false).one(jsObj("company").as[JsObject]))

        }
        .runWith(Sink.ignore)
    }
  }

  def findIllegal() = {
    detailcoll.map(companies).map(_._1).map { source =>
      source
        .map(d => (d("_id"), d("content").validate[String].getOrElse("")))
        .filter(tuple => tuple._2.nonEmpty)
        .map { tuple =>
          val r = tuple._2.split("""\n""").toList.
            foldLeft(init)(_ read _)
            .extracted
            .getOrElse("")
          (tuple._1, r)
        }.filter(_._2.isEmpty)
        .map { tuple =>
          println(tuple._1)
        }
        .runWith(Sink.ignore)
    }

  }

  //  findIllegal()
  //  export2()


}
