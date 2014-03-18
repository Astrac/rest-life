import sbt._
import Keys._

object AutomataBuild extends Build {
  lazy val scalaFx = "org.scalafx" % "scalafx_2.10" % "1.0.0-M7"
  lazy val scalaTest = "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

  // ScalaFX
  lazy val scalaFxSettings = Seq(
    unmanagedJars in Compile += Attributed.blank(
      file(System.getenv("JAVA_HOME") + "/jre/lib/jfxrt.jar")),
    fork in run := true
  )

  lazy val lsgu_automata = Project(id = "lsgu-automata", base = file("./"),
    settings = Project.defaultSettings ++ scalaFxSettings ++ Seq(
      scalaVersion := "2.10.3",
      sbtVersion := "0.13.1",
      scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
      libraryDependencies ++= Seq(
        scalaTest,
        scalaFx
      )))
}
