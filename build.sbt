name := "shooter-multiplayer"

version := "1.0"

scalaVersion := "2.12.7"

libraryDependencies += "org.scalafx" % "scalafx_2.12" % "8.0.102-R11"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.18"

libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.5.18"

libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.5"

libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.18"

unmanagedResourceDirectories in Compile += { baseDirectory.value / "src/main/resources/" }

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
