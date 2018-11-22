package software.xuanruanjian

import software.XRJProductParser
import software.XRJProductParser.Product

import scala.io.Source

object DetailParser extends App {

  trait DetailState {
    def product: XRJProductParser.Product

    def read(line: String): DetailState
  }

  case class SeekingDetail(product: XRJProductParser.Product) extends DetailState {
    val prod = """<div class="prod">"""

    override def read(line: String): DetailState =
      if (line contains prod) ProductStarted(product)
      else this
  }

  case class ProductStarted(product: XRJProductParser.Product) extends DetailState {
    val name = """.*?<h3>(.*?)</h3>.*""".r

    override def read(line: String): DetailState = line match {
      case name(n) => ProductNameLoaded(product.copy(
        title = n
      ))
      case _ => this
    }
  }

  case class ProductNameLoaded(product: XRJProductParser.Product) extends DetailState {
    val company = """.*?<li>开发商：(.*?)</li>.*?<a href=\"/tag/.*\">(.*?)</a>.*""".r
    val company2 = """.*?<li>开发商：(.*?)</li>.*?<a href=\"/soft/.*\">(.*?)</a>.*""".r

    override def read(line: String): DetailState = line match {
      case company(name, secondaryCate) => Done(product.copy(companyName = name, secondaryCategory = secondaryCate))
      case company2(name, secondaryCate) => Done(product.copy(companyName = name, secondaryCategory = secondaryCate))
      case _ => this
    }
  }

  case class Done(product: XRJProductParser.Product) extends DetailState {
    override def read(line: String): DetailState = this
  }

  println(
    Source.fromFile("/Users/barry/Workspaces/frank/allied/src/main/resources/detail.html", "UTF8")
      .getLines()
      .foldLeft[DetailState](SeekingDetail(Product()))((s, l) => s.read(l))
  )
}
