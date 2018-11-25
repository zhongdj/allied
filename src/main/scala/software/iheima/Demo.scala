package software.iheima

import java.io.{File, FileWriter}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import software.xuanruanjian.PaginationSpider._
import software.xuanruanjian.Spider

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source

object Demo extends App {
//  implicit val system = ActorSystem()
//  implicit val materializer = ActorMaterializer()
//
//  val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "http://www.iheima.com/enterprise-library/detail/14783"))
//  responseFuture.foreach { res =>
//    res.entity.dataBytes.fold("")(_ ++ _.utf8String)
//      .runForeach(println)
//  }

  val downloaded  = new File("/Users/barry/Workspaces/frank/allied/iheima/detail/").listFiles()
    .toList
    .map(file => file.getName)
    .toSet

  (1 to 14783)
    .filter(i => !downloaded.contains(i.toString))
    .toStream
    .map(i => (i, s"http://www.iheima.com/enterprise-library/detail/${i}"))
    .map { s => println(s); s }
    .foreach { i =>
      val index = i._1
      doSpider(index, i._2)
    }

  //Int => String => URL => Future
  def doSpider: (Int, String) => Unit = (index, url) => {
    val fullFileName = "/Users/barry/Workspaces/frank/allied/iheima/detail/" + index
    val file = new File(fullFileName)
    if (file.exists()) {
      file.delete()
      file.createNewFile()
      println(s"writing file ${file}")
    } else {
      file.createNewFile()
      println(s"writing file ${file}")
    }
    val out = new FileWriter(fullFileName, true)
    try {
      val s = Source.fromURL(url, "UTF8").getLines()
        .mkString("\n")
      out.write(s)
    } catch {
      case _ =>
        try {
          val s = Source.fromURL(url, "GBK").getLines().mkString("\n")
          out.write(s)
        } catch {
          case e => e.printStackTrace()
        }
    } finally {
      out.flush()
      out.close()
    }
  }

}