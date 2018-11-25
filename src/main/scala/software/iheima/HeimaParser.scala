package software.iheima

import java.io.File

import io.happy.Spider.ExtractorState
import io.happy.StateBuilder._
import reactivemongo.api.MongoConnection
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument

import scala.concurrent.{Await, Future}
import scala.io.Source

object HeimaParser extends App {

  case class CompanyHeima(seq: String = "",
                          name: String = "",
                          desc: String = "",
                          category: String = "",
                          site: String = ""
                         ) {
    def isFull(): Boolean = {
      seq.nonEmpty &&
        name.nonEmpty &&
        desc.nonEmpty &&
        category.nonEmpty &&
        site.nonEmpty
    }

    def onlySiteIsEmpty(): Boolean = {
      seq.nonEmpty &&
        name.nonEmpty &&
        desc.nonEmpty &&
        category.nonEmpty &&
        site.isEmpty
    }

    def cate: String =
      if (category.contains("-")) category.substring(0, category.indexOf("-"))
      else category

    def subCate: String =
      if (category.contains("-")) category.substring(category.indexOf("-") + 1)
      else ""
  }

  val start = """<div class="enterprise-desc">""".r
  val name = """.*?<h3 class="name">(.+?)</h3>""".r
  val desc = """.*?<p class="desc">(.+?)</p>""".r
  val cate = """\s+(.*?)</li>.*""".r
  val site = """.*?<li>(.*?)</li>.*""".r

  val init = initialize[CompanyHeima] {
    case (p, start(_*)) => p
  }.andThen {
    case (p, name(n)) => p.copy(name = n)
  }.andThen {
    case (p, desc(d)) => p.copy(desc = d)
  }.andThen {
    case (p, cate(c)) => p.copy(category = c.replaceAll("""\s""", ""))
  }.andThen {
    case (p, site(s)) => p.copy(site = s)
  }.build(CompanyHeima())
  /*
    val data = (1 to 14783).toStream.map(i => (i, fileName(i.toString)))
      .map { file =>
        parse(new File(file._2))
      }.filter(_.isFull)
  */

  val data = new File("/Users/barry/Workspaces/frank/allied/iheima/detail/")
    .listFiles()
    .filter(_.getName != ".DS_Store")
    .toStream
    .map(parse)
    .toList
  /*
  //.filter(_.onlySiteIsEmpty)
  .map { c =>
    fileName(c.seq)
  }.foreach { file =>
  new File(file).delete()
}
*/

  private def parse(theFile: File) = {
    Source.fromFile(theFile, "UTF-8")
      .getLines()
      .foldLeft[ExtractorState[CompanyHeima]](init)(_ read _)
      .extracted
      .get.copy(seq = theFile.getName())
  }

  private def fileName(i: String)

  = {
    s"/Users/barry/Workspaces/frank/allied/iheima/detail/${i}"
  }

  def writeMongo(startups: List[CompanyHeima]) = {
    import scala.concurrent.ExecutionContext.Implicits.global // use any appropriate context

    // Connect to the database: Must be done only once per application
    val driver = new reactivemongo.api.MongoDriver
    val connection: MongoConnection = driver.connection(List("localhost:27017"))
    val coll: Future[BSONCollection] = connection.database("company").map(_.collection("startup"))
    // or provide a custom one

    val bson = startups.map { c =>
      println(c)
      BSONDocument(
        "seq" -> c.seq,
        "category" -> c.cate,
        "subCategory" -> c.subCate,
        "name" -> c.name,
        "website" -> c.site,
        "foundOn" -> "",
        "investment" -> "",
        "location" -> ""
      )
    }

    import scala.concurrent.duration._

    Await.result(
      coll.map {
        col =>
          col.insert(false)
            .many(bson)
      }, 10 minutes)
  }

  writeMongo(data)
}
