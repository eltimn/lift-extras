import sbt._
import sbt.Keys._

object LiftModule {
  val liftVersion = settingKey[String]("Lift Web Framework full version number")
  val liftEdition = settingKey[String]("Lift Edition (such as 2.6 or 3.0)")
}
