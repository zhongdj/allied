package software.iheima

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Demo extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "https://akka.io/docs/?_ga=2.212085277.84893290.1542725311-1136405608.1542725194"))
  responseFuture.foreach { res =>
    res.entity.dataBytes.fold("")(_ ++ _.utf8String)
      .runForeach(println)
  }

//Int => String => URL => Future


}