package example

import java.io.InputStream
import java.net.URL
import java.util.concurrent.{ExecutorService, Executors}

import scala.concurrent.{Future, Promise}
import com.ning.http.client._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source

object Hello extends Greeting with App {

  val xs : List[(Int, String)] = 812400 to 2000000 map (id => (id, s"http://weixinqun.com/group?id=${id}")) toList

//  val groups = Future.sequence(xs.map(tuple => toContent(tuple._2)(tuple._1)))//.map(toGroups)
//
//  groups.foreach(gs => gs.filter(!_.isEmpty).foreach(g => println(g.get)))


//  xs.foreach(tuple => toContent(tuple._2)(tuple._1))


  type GroupSegment = String

  private def toSegment(inputStream: InputStream)(implicit id: Int): Option[Group] = Source.fromInputStream(inputStream, "UTF-8").getLines().foldLeft[ContentReader](EmptyReader(id)){ case (reader, line) => reader.read(line)}.getGroup

//  private def toContent(link: String)(implicit id: Int)= {
//    fetchPage(link).map(toSegment).foreach(og => if (!og.isEmpty) println(og.get))
//  }
//  private def fetchPage(link: String) = {
//    val cf = new AsyncHttpClientConfigBean
//    //    cf.setMaxConnectionPerHost(100)
//    cf.setMaxConnectionLifeTimeInMs(500)
//    val client = new AsyncHttpClient(cf)
//    var result = Promise[InputStream]()
//
//    val request = client.prepareGet(link).build()
//    client.executeRequest(request, new AsyncCompletionHandler[Response]() {
//      override def onCompleted(response: Response) = {
//        result.success(response.getResponseBodyAsStream)
//        //client.close()
//        response
//      }
//
//      override def onThrowable(t: Throwable) {
//        result.failure(t)
//        //client.close()
//      }
//    })
//    result.future
//  }


  val executorService = Executors.newFixedThreadPool(200)

  xs.foreach {
    case (id: Int, link: String) =>
      executorService.submit(new Runnable {
        override def run(): Unit = toSegment(new URL(link).openConnection().getInputStream)(id).foreach(println)
      })

  }

}

case class Group(id: Int, squareCode: String, name: String, description: String, industry: String, location: String, createdOn: String, popularity: Int, labels: List[String] = Nil) {
  override def toString = s"${id}_|_${squareCode}_|_${name}_|_${description}_|_${industry}_|_${location}_|_${createdOn}_|_${popularity}_|_${labels.mkString(",")}"
}

trait Greeting {
  lazy val greeting: String = "hello"


}
