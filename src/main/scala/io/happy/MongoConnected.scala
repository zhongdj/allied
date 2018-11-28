package io.happy

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import reactivemongo.api.MongoConnection

trait MongoConnected {




  // use any appropriate context
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  val driver = new reactivemongo.api.MongoDriver
  val connection: MongoConnection = driver.connection(List("localhost:27017"))
}
