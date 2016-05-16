name := "money-transfer"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.20",
  "ch.qos.logback" % "logback-classic" % "1.1.7",

  "io.netty" % "netty-all" % "4.0.36.Final",

  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)
