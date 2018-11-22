package software.xuanruanjian

import scala.io.Source
import scala.util.matching.Regex

object XRJParser extends App {

  /*

  Parser --> StateMachine --> T

  Parser
    object {
      - many[T](Parser[T]): Parser[Stream[T]]
    }

    class[T] {
      - begin(regex): Parser[T]
      - read(regex): Parser[T]
      - readM(regex):
      - end(regex)
      - runOn(Stream[String]): Parser[T]
    }

   StateMachine[T]
      - initialState: State[T]
      - read(String): State[T]
      - currentState: State[T]

   State[T, S](run: S => (T,S))
      - read(String): State[T, S]
      - get: Either[Partial[T], T]

   */

  trait CateState {
    def read(line: String): CateState

    def acc: List[Cate]
  }

  case class Initializing(acc: List[Cate] = Nil) extends CateState {
    override def read(line: String): CateState = {
      if (line.contains("""<div class="cate">""")) Initialized(acc)
      else if (acc.size > 0 && line.contains("""javascript""")) Done(acc)
      else this
    }
  }

  val cate  = """.+?<a href=\"([^javascript|http|\"].+?)\".+?>(.+?)<.*""".r

  case class Initialized(acc: List[Cate]) extends CateState {
    override def read(line: String): CateState = line match {
      case cate(url, title) => ReadingChildren(acc, Cate(title, url))
      case _ =>
        if (acc.size > 0 && line.contains("""javascript""")) Done(acc)
        else this
    }
  }

  case class ReadingChildren(acc: List[Cate], category: Cate) extends CateState {
    val end: Regex = """.+?</div>""".r

    override def read(line: String): CateState = line match {
      case cate(url, title) => this.copy(
        category = category.copy(
          children = (Cate(title, url) :: category.children)
        )
      )
      case end(_*) => Initializing(category :: acc)
      case _ => this
    }
  }

  case class Done(acc: List[Cate]) extends CateState {
    override def read(line: String): CateState = this
  }

  case class Cate(title: String, url: String, children: List[Cate] = Nil) {
    override def toString: String = s"Cate(title = ${title}, url = ${url}${if (children.isEmpty) ")" else ", children = \n\t" + children.mkString("\n\t")}"
  }

  Source.fromFile("/Users/barry/Workspaces/frank/allied/src/main/resources/xuanruanjian.html", "GB2312")
    .getLines()
    .foldLeft[CateState](Initializing())((s, l) => s.read(l))
    .acc
    .foreach(println)
}
