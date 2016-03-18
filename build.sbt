name := "PlayRestClient"

version := "1.0"

lazy val `playrestclient` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test )

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.2"

libraryDependencies += "org.apache.httpcomponents" % "httpcore" % "4.4.4"

libraryDependencies += "com.google.code.gson" % "gson" % "2.6.2"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"  