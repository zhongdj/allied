package software.xuanruanjian

import java.io.{File, FileWriter}

import scala.io.Source

object Spider extends App {

  def host = "https://www.xuanruanjian.com"

  def urls = Source.fromFile("/Users/barry/Workspaces/frank/allied/src/main/resources/urls.txt")
    .getLines()
    .map(host + _)

  urls.filter(_.contains("soft")).toList.foreach {
    url => {
      val folder = if (url.contains("soft")) "soft" else "tag"
      val justFileName = url.substring(url.lastIndexOf("/"))
      val fullFileName = "/Users/barry/Workspaces/frank/allied/xuanruanjian/" + folder + "/" + justFileName
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
        val s = Source.fromURL(url, "GB2312").getLines()
          .mkString("\n")
        out.write(s)
      } catch {
        case _ =>
          val s = Source.fromURL(url, "GBK").getLines().mkString("\n")
          out.write(s)
      }
      out.flush()
      out.close()
      /*
       try {
         val url2 = if (url.endsWith("html")) url else url + "/"

         val br = Source.fromURL(url2, "GB2312")
           .bufferedReader()
         var s = ""
         do {
           s = br.readLine()
           out.write(s + "\n")
         } while (s != null)

       } catch {
         case t: Throwable => t.printStackTrace()

       } finally {
         out.flush()
         out.close()
       }
       */
    }
  }


}
