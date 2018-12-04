name := "shooter-multiplayer"

version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies += "org.scalafx" % "scalafx_2.12" % "8.0.102-R11"

unmanagedResourceDirectories in Compile += { baseDirectory.value / "src/main/resources/" }

fork in run := true

connectInput in run := true

offline := true
