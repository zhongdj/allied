package hr.zhaopin.company

import java.io.PrintWriter
import java.net.URL

import hr.zhaopin.company.reader.{CompanyReader, EmptyReader}

import scala.io.Source

object Main extends App {

  val writer = new PrintWriter("result.txt")

  PageRequest.data.par.flatMap { link =>

    try {
      val inputStream = new URL(link.toString).openConnection().getInputStream

      Source.fromInputStream(inputStream, "UTF-8").getLines()
        .foldLeft[CompanyReader](EmptyReader(link.city)) { case (reader, line) => reader.read(line) }
        .acc

    } catch {
      case t: Throwable => Nil
    }
  }.foreach(writer.println)
  writer.flush()

  Thread.sleep(60000)

}
