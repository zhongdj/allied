import Dependencies._
import sbt.Keys.libraryDependencies

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.7",
      version := "0.1.0-SNAPSHOT"
    )),
    name := "Hello",
    resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    libraryDependencies ++= Seq(
      "org.reactivemongo" % "reactivemongo_2.12" % "0.16.0",
      "org.reactivemongo" %% "reactivemongo-akkastream" % "0.16.0"),
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.8",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % "10.1.5",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.1.5" % Test
    ),
    libraryDependencies ++= Seq(
      "com.ning" % "async-http-client" % "1.8.13",
      "ch.qos.logback" % "logback-classic" % "1.0.7"),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream" % "2.5.18",
      "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.18" % Test
    )
  )
