package software.xuanruanjian

import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID, BSONString, document, array}
import software.XRJProductParser.Product
import software.xuanruanjian.DetailParser.{DetailState, SeekingDetail}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.Source
import scala.concurrent.duration._

object CompanyName extends App {

  import ExecutionContext.Implicits.global // use any appropriate context

  // Connect to the database: Must be done only once per application
  val driver = new reactivemongo.api.MongoDriver
  val connection: MongoConnection = driver.connection(List("localhost:27017"))
  val coll: Future[BSONCollection] = connection.database("company").map(_.collection("product"))
  // or provide a custom one

  def listProducts(): Future[List[BSONDocument]] =
    coll.flatMap(_.find(document(
      "$or" -> array(
        document("companyName" -> ""),
        document("secondaryCategory" -> "")
      )
    ))
      .cursor[BSONDocument]()
      .collect[List](-1, Cursor.FailOnError[List[BSONDocument]]()))


  def update1(personColl: BSONCollection) = {
    val selector = BSONDocument("name" -> "Jack")

    val modifier = BSONDocument(
      "$set" -> BSONDocument(
        "lastName" -> "London",
        "firstName" -> "Jack"),
      "$unset" -> BSONDocument("name" -> 1))

    // Simple update: get a future update
    val futureUpdate1 = personColl.
      update(ordered = false).one(selector, modifier,
      upsert = false, multi = false)

    // Bulk update: multiple update
    val updateBuilder1 = personColl.update(ordered = true)
    val updates = Future.sequence(Seq(
      updateBuilder1.element(
        q = BSONDocument("firstName" -> "Jane", "lastName" -> "Doh"),
        u = BSONDocument("age" -> 18),
        upsert = true,
        multi = false),
      updateBuilder1.element(
        q = BSONDocument("firstName" -> "Bob"),
        u = BSONDocument("age" -> 19),
        upsert = false,
        multi = true)))

    val bulkUpdateRes1 = updates.flatMap { ops => updateBuilder1.many(ops) }
  }

  Await.result(listProducts(), 10 seconds).map { p =>
    val id = p.getAs[BSONObjectID]("_id").get
    val url = p.getAs[BSONString]("productLink").get
    val product = pageContent(url.value)
      .foldLeft[DetailState](SeekingDetail(Product()))((s, l) => s.read(l))
      .product
    println(s"${id}, ${product.companyName}, ${product.secondaryCategory}")
    val selector = BSONDocument("_id" -> id)
    val modifier = BSONDocument("$set" -> BSONDocument(
      "secondaryCategory" -> product.secondaryCategory,
      "companyName" -> product.companyName
    ))
    //    val modifier = BSONDocument("$set" -> BSONDocument("companyName" -> name))
    val futureUpdate = coll.flatMap(c => c
      .update(ordered = false)
      .one(selector, modifier, upsert = false, multi = false)
    )
    println(Await.result(futureUpdate, 10 seconds))
  }

  private def pageContent(url: String) = {
    try {
      Source.fromURL(url, "GBK").getLines().toList
    } catch {
      case _ =>
        try {
          Source.fromURL(url, "GB2312").getLines().toList
        } catch {
          case _ =>
            try {
              Source.fromURL(url, "UTF8").getLines().toList
            } catch {
              case _ =>
                println(s"failed to load: ${
                  url
                }")
                List("")
            }
        }
    }
  }

  Thread.sleep(60000000)
}
