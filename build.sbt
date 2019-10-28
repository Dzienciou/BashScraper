name := "finitech"

mainClass in (Compile, run) := Some("Scraper")

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "4.6.0" % "test",
  "com.typesafe" % "config" % "1.3.2",
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.1.0-M7",
  "com.typesafe.play" %% "play-ws-standalone-json" % "2.1.0-M7",
  "org.jsoup" % "jsoup" % "1.11.3",
  "com.typesafe.akka" %% "akka-actor" % "2.5.26",
  "nl.grons" %% "metrics4-scala" % "4.1.1"
)

scalacOptions in Test ++= Seq("-Yrangepos")
