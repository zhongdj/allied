package bigdata

import reactivemongo.api.MongoConnection
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument

import scala.concurrent.{Await, Future}
import scala.io.Source

object BigDataReader extends App {

  case class Company(seq: String, cat1: String, cat2: String, name: String, found: String, amount: String, location: String)

  private val lines: List[String] = Source.fromFile("/Users/barry/Workspaces/frank/allied/bigdata.txt", "UTF-8")
    .getLines.toList


  import scala.concurrent.ExecutionContext.Implicits.global // use any appropriate context

  // Connect to the database: Must be done only once per application
  val driver = new reactivemongo.api.MongoDriver
  val connection: MongoConnection = driver.connection(List("localhost:27017"))
  val coll: Future[BSONCollection] = connection.database("company").map(_.collection("bigdatacompany"))
  // or provide a custom one

  val data = lines
    .map { line =>
      val values = line.split("""\t""")
      println(values.toList.mkString(","))
      val str = values(5)
      var amount = ""
      var city = ""
      if (values.length == 7) {
        amount = str
        city = values(6)
      } else {
        amount = "-1"
        city = str
      }
      Company(values(0), values(1), values(2), values(3), values(4), str, city)
    }.map { c =>
    BSONDocument(
      "seq" -> c.seq,
      "category" -> c.cat1,
      "subCategory" -> c.cat2,
      "name" -> c.name,
      "foundOn" -> c.found,
      "investment" -> c.amount,
      "location" -> c.location
    )
  }

  import scala.concurrent.duration._

  Await.result(
    coll.map {
      col =>
        col.insert(false)
          .many(data)
    }, 10 minutes)
}
