package models

import scala.slick.driver.MySQLDriver.simple._

case class Class(id: Option[Int], name: String)

object Classes extends Table[Class]("CLASS") {
  def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def * = id ~ name <> (Class, Class.unapply _)
  def autoInc = * returning id

  def add(name: String)(implicit s: Session) {
    Classes.autoInc.insert(new Class(None, name))
  }

}