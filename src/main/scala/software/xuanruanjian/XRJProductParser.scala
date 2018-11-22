package software

import play.api.libs.json.Json

import scala.io.Source

object XRJProductParser extends App {

  case class Product(imageUrl: String = "",
                     title: String = "",
                     productLink: String = "",
                     category: String = "",
                     secondaryCategory: String = "",
                     targetCustomers: List[String] = Nil,
                     description: String = "",
                     keywords: List[String] = Nil,
                     companyName: String = ""
                    )

  object Product {
    implicit val format = Json.format[Product]
  }

  trait ProductState {
    def acc: List[Product]

    def read(line: String): ProductState
  }

  case class Seeking(acc: List[Product] = Nil) extends ProductState {
    val segment = """<dl class="item clearfix">"""

    override def read(line: String): ProductState =
      if (line contains segment) ProductStarted(acc)
      else this
  }

  case class ProductStarted(acc: List[Product]) extends ProductState {
    val links = """.*<a href=\"(.+?)\".+?<img src=\"(.+?)\".*""".r

    override def read(line: String): ProductState = line match {
      case links(prodLink, imgLink) => ImageUrlLoaded(acc, Product(
        imageUrl = imgLink,
        productLink = prodLink
      ))
      case _ => this
    }

  }

  case class ImageUrlLoaded(acc: List[Product], partial: Product) extends ProductState {
    val title = """.+?<div class="tit"><a .+?>(.+?)</a>.*""".r

    override def read(line: String): ProductState = line match {
      case title(aTitle) => TitleLoaded(acc, partial.copy(title = aTitle))
      case _ => this
    }
  }

  case class TitleLoaded(acc: List[Product], partial: Product) extends ProductState {
    val targets = """.+?<span>软件类别：(.+?)</span>.*""".r

    override def read(line: String): ProductState = line match {
      case targets(cat) => CategoryLoaded(acc, partial.copy(
        category = cat
      ))
      case _ => this

    }
  }

  case class CategoryLoaded(acc: List[Product], partial: Product) extends ProductState {
    val potentials = """.*?<span.+?>适用企业：(.*?)</span>""".r

    override def read(line: String): ProductState = line match {
      case potentials(opps) => TargetCustomerLoaded(acc, partial.copy(
        targetCustomers = opps.split(" ").filter(_.nonEmpty).toList
      ))
      case _ => this
    }
  }

  case class TargetCustomerLoaded(acc: List[Product], partial: Product) extends ProductState {
    val des = """.+?<div class="des">(.+?)</div>""".r
    val des2 = """.+?<div class="des">(.+?)""".r

    override def read(line: String): ProductState = line match {
      case des(comments) =>
        DescriptionLoaded(acc, partial.copy(
          description = comments.replace("&nbsp;", "")
        ))
      case des2(comments) =>
        DescriptionLoading(acc, partial.copy(
          description = comments.replaceAll("&nbsp;", "")
        ))
      case _ => this
    }
  }

  case class DescriptionLoading(acc: List[Product], partial: Product) extends ProductState {
    val endSegment = """</div>"""

    override def read(line: String): ProductState =
      if (line contains endSegment)
        DescriptionLoaded(acc, partial.copy(
          description = partial.description + "<br/>" + line.replace(endSegment, "")
        ))
      else DescriptionLoading(acc, partial.copy(
        description = partial.description + "<br/>" + line
      ))
  }

  case class DescriptionLoaded(acc: List[Product], partial: Product) extends ProductState {
    val segments = """<div class="cl">"""

    override def read(line: String): ProductState =
      if (line contains segments) LoadingKeywords(acc, partial)
      else this
  }

  case class LoadingKeywords(acc: List[Product], partial: Product) extends ProductState {
    override def read(line: String): ProductState = Seeking(
      partial.copy(keywords = line.split(" ").toList.filter(_.nonEmpty)) :: acc
    )
  }

  case class Done(acc: List[Product]) extends ProductState {
    override def read(line: String): ProductState = this
  }

  Source.fromFile("/Users/barry/Workspaces/frank/allied/src/main/resources/products.html", "GB2312")
    .getLines()
    .foldLeft[ProductState](Seeking())((s, l) => s.read(l))
    .acc
    .foreach(println)
}
