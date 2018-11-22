package software.xuanruanjian

import software.XRJProductParser
import software.XRJProductParser.Product
import software.xuanruanjian.DetailParser2.Esi.parse

import scala.io.Source
import scala.util.matching.Regex

object DetailParser2 extends App {

  type ExtractorPair[T] = (Regex, List[String] => ExtractorState[T])

  trait ExtractorState[T] {

    def read(line: String): ExtractorState[T] = (goto orElse stay) (line)

    lazy val stay: PartialFunction[String, ExtractorState[T]] = {
      case _ => this
    }

    def goto: PartialFunction[String, ExtractorState[T]]

    def extracted: Option[T]
  }

  case class Esi[T](goto: PartialFunction[String, ExtractorState[T]], extracted: Option[T] = None) extends ExtractorState[T]

  case class RegexState[T](pattern: String, goto: PartialFunction[String, ExtractorState[T]], extracted: Option[T]) extends ExtractorState[T] {
    val regex = pattern.r
  }

  case class Regex2State[T](pattern1: String, pattern2: String, goto: PartialFunction[String, ExtractorState[T]], extracted: Option[T]) extends ExtractorState[T] {
    val regex1 = pattern1.r
    val regex2 = pattern2.r
  }


  object Esi {
    def parse[T](goto: PartialFunction[String, ExtractorState[T]])(implicit extracted: Option[T] = None): Esi[T] = Esi(goto, extracted)
  }

  type Prod = XRJProductParser.Product

  val prod = """<div class="prod">""".r
  val name = """.*?<h3>(.*?)</h3>.*""".r
  val company = """.*?<li>开发商：(.*?)</li>.*?<a href=\"/tag/.*\">(.*?)</a>.*""".r
  val company2 = """.*?<li>开发商：(.*?)</li>.*?<a href=\"/soft/.*\">(.*?)</a>.*""".r

  def seeking(): ExtractorState[Prod] = parse[Prod] {
    case line if (line contains prod) => productStarted(XRJProductParser.Product())
  }

  def productStarted(product: Prod) = parse[Prod] {
    case name(n) => nameLoaded(product.copy(title = n))
  }

  def nameLoaded(product: Prod) = parse[Prod] {
    case company(name, secondaryCate) => done(product.copy(companyName = name, secondaryCategory = secondaryCate))
    case company2(name, secondaryCate) => done(product.copy(companyName = name, secondaryCategory = secondaryCate))
  }

  def done(product: Prod) = parse(PartialFunction.empty)(Some(product))
/*
  println(
    Source.fromFile("/Users/barry/Workspaces/frank/allied/src/main/resources/detail.html", "UTF8")
      .getLines()
      .foldLeft[ExtractorState[Prod]](seeking())((s, l) => s.read(l))
      .extracted
  )
*/

  import StateBuilder._

  val prodExtractor: ExtractorState[Prod] =
    initialize[Prod] { case (p, prod(_)) => p }
      .andThen { case (p, name(n)) => p.copy(title = n) }
      .andThen {
        case (p, company(c, s)) => p.copy(companyName = c, secondaryCategory = s)
        case (p, company2(c, s)) => p.copy(companyName = c, secondaryCategory = s)
      }.build(Product())

  println(
    Source.fromFile("/Users/barry/Workspaces/frank/allied/src/main/resources/detail.html", "UTF8")
      .getLines()
      .foldLeft[ExtractorState[Prod]](prodExtractor)((s, l) => s.read(l))
      .extracted
  )


}
