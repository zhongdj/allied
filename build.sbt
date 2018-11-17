import Dependencies._
import sbt.Keys.libraryDependencies

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.7",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Hello",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= Seq(
      "com.ning" % "async-http-client" % "1.8.13",
      "ch.qos.logback" % "logback-classic" % "1.0.7")
  )
