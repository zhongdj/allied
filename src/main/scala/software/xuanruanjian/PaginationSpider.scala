package software.xuanruanjian

import java.io.{File, FileWriter}

import scala.io.Source

object PaginationSpider extends App {

  case class Pagination(name: String = "", on: Int = 1, last: Int = 1)

  case class PaginationRequest(name: String, index: Int)

  // folder file => List files foreach file => (name, pagination) => Option[index] foreach => create & writeFile

  val folder = "/Users/barry/Workspaces/frank/allied/xuanruanjian/soft"
  val folderFile = new File(folder)

  def toPagination: File => Option[Pagination] = file =>
    Source.fromFile(file)
      .getLines()
      .foldLeft[PaginationState](SeekingPagination(file.getName))((b, l) => b.read(l))
      .get

  def doSpider: PaginationRequest => Unit = page => {
    val fullFileName = "/Users/barry/Workspaces/frank/allied/xuanruanjian/pagination/soft" + page.name + "." + page.index
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
    val url = Spider.host + "/soft/" + page.name + "?p=" + page.index
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
  }

  def toPaginationRequest: Pagination => List[PaginationRequest] = {
    case Pagination(name, _, last) =>
      (2 to last).toList.map(i => PaginationRequest(name, i))
  }

  def fetchNextPagination(p: Pagination): Option[Pagination] = {
    println(s"fetch next: ${p}")
    val fileName = p.name + "?p=" + p.last
    val url = Spider.host + "/soft/" + fileName
    try {

      Source.fromURL(url, "GB2312")
        .getLines()
        .foldLeft[PaginationState](SeekingPagination(fileName))((b, l) => b.read(l))
        .get match {
        case Some(Pagination(name, _, l)) if l == p.last => Some(Pagination(name, l, l))
        case _ => Some(p.copy(on = p.last))
      }
    } catch {
      case _ =>
        Source.fromURL(url, "GBK")
          .getLines()
          .foldLeft[PaginationState](SeekingPagination(fileName))((b, l) => b.read(l))
          .get match {
          case Some(Pagination(name, _, l)) if l == p.last => Some(Pagination(name, l, l))
          case _ => Some(p.copy(on = p.last))
        }
    }
  }

  def findLastPagination: Option[Pagination] => Option[Pagination] = {
    case p@Some(Pagination(_, on, last)) if on == last => p
    case Some(p) => findLastPagination(
      fetchNextPagination(p)

    )
    case _ => None
  }

  folderFile.listFiles()
    .toStream
    //.drop(10).take(5)
    .map { s => println(s.getName); s }
    .map(toPagination)
    .map(findLastPagination)
    .flatMap(_.map(toPaginationRequest).getOrElse(Nil))
    .foreach(doSpider)


  trait PaginationState {
    def read(line: String): PaginationState

    def get: Option[Pagination] = None
  }


  case class SeekingPagination(name: String) extends PaginationState {
    val segment = """<div class="pagination">"""

    override def read(line: String): PaginationState =
      if (line contains segment) PaginationStarted(name)
      else this
  }

  case class PaginationStarted(name: String) extends PaginationState {
    val last = """.*class=\"on\">(.+?)</a>.*>(.*)?</a></span>.*""".r

    override def read(line: String): PaginationState = line match {
      case last(on, page) => PaginationDone(Pagination(name, on.toInt, page.toInt))
      case _ => this
    }
  }

  case class PaginationDone(pg: Pagination) extends PaginationState {
    override def read(line: String): PaginationState = this

    override def get: Option[Pagination] = Some(pg)
  }

}
