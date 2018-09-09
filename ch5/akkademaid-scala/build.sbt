name := "akkademaid-scala"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.akkademy-db"   %% "akkademy-db" % "0.0.1-SNAPSHOT",
  "com.syncthemall" % "boilerpipe" % "1.2.2",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.6" % "test",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test"
)