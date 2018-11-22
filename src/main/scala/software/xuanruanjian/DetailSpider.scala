package software.xuanruanjian

import java.io.{File, FileWriter}

import software.XRJProductParser.{ProductState, Seeking}

import scala.io.Source

object DetailSpider extends App {

  val folder1 = "/Users/barry/Workspaces/frank/allied/xuanruanjian/soft/"
  val folder2 = "/Users/barry/Workspaces/frank/allied/xuanruanjian/pagination/"

  val files: List[File] = new File(folder1).listFiles().toList ::: new File(folder2).listFiles().toList

  val productFolder = "/Users/barry/Workspaces/frank/allied/xuanruanjian/product/"

  files.toStream
    .flatMap { file =>
      lines(file)
        .foldLeft[ProductState](Seeking())((s, l) => s.read(l))
        .acc
        .map(_.productLink)
        .toStream
    }.par.foreach { url: String =>

    println(s"loading: ${url}")
    val fileName = url.substring(url.lastIndexOf("/"))
    val file = new File(productFolder, fileName)
    if (file.exists()) {
      println(s"${file.getName} is already got")
    } else {
      println(s"writing: ${file.getName}")
      file.createNewFile()
      val out = new FileWriter(file, true)
      try {
        val s = Source.fromURL(url, "GB2312").getLines()
          .mkString("\n")
        out.write(s)
      } catch {
        case _ =>
          try {
            val s = Source.fromURL(url, "GBK").getLines().mkString("\n")
            out.write(s)
          } catch {
            case _ =>
              try {
                val s = Source.fromURL(url, "UTF8").getLines().mkString("\n")
                out.write(s)
              } catch {
                case _ =>
                  println(s"failed to load: ${url}")
              }
          }
      } finally {
        out.flush()
        out.close()
      }
    }
  }


  private def lines(file: File): Stream[String] = {

    try {
      Source.fromFile(file).getLines().toStream
    } catch {
      case _ =>
        try {
          Source.fromFile(file, "GBK").getLines().toStream
        } catch {
          case _ =>
            try {
              Source.fromFile(file, "GB2312").getLines().toStream
            } catch {
              case _ =>
                println("failed to read file: " + file)
                Stream.empty
            }
        }
    }

  }
}
