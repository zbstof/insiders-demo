name := "zbstsearch"

version := "1.0"

lazy val `zbstsearch` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  jdbc,
  ehcache,
  ws,
  specs2 % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test",
  guice)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

      