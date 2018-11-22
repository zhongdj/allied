package software.xuanruanjian

import java.io.{File, PrintWriter}

import hr.zhaopin.company.Main.writer
import play.api.libs.json.Json
import software.XRJProductParser
import software.XRJProductParser.{ProductState, Seeking}

import scala.io.Source
import scala.util.Try

object SRJMain extends App {

  private val seed = "/Users/barry/Workspaces/frank/allied/xuanruanjian/soft"
  private val pages = "/Users/barry/Workspaces/frank/allied/xuanruanjian/pagination"

  private def listFiles(folder: String) = new File(folder).listFiles().toList

  val listPageFiles = listFiles(seed) ::: listFiles(pages)


  private def productStream(file: File)(enc: String): Try[Stream[XRJProductParser.Product]] = {
    Try {
      val acc = Source.fromFile(file, enc)
        .getLines()
        .foldLeft[ProductState](Seeking())((s, l) => s.read(l))
        .acc

      //println(s"file: ${file.getName}, size: ${acc.size}")

      acc.toStream
    }
  }

  println(listPageFiles.size)

  val writer = new PrintWriter("product.json")
  listPageFiles.toStream
    .flatMap { file =>
      //println(s"processing: ${file.getAbsolutePath}")
      val f: String => Try[Stream[XRJProductParser.Product]] = productStream(file)
      f("GB2312")
        .recoverWith {
          case _ => f("GBK")
        }
        .recoverWith {
          case _ => f("UTF8")
        }
        .recoverWith {
          case e =>
            //println("*************************8")
            f("UTF8")
        }
        .getOrElse(Stream.empty)
    }.foreach(p => writer.println(Json.toJson(p).toString))
  writer.flush()

  Thread.sleep(60000)
  /**/
}
