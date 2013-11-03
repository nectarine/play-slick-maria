package models

import scala.slick.driver.MySQLDriver.simple._

case class Student(id: Option[Int], name: String, classId: Option[Int])

object Students extends Table[Student]("STUDENT") {
  def id = column[Option[Int]]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
  def classId = column[Option[Int]]("CLASS")
  def * = id ~ name ~ classId <> (Student, Student.unapply _)

  def clazz = foreignKey("FK_CLASS", classId, Classes)(_.id)
  def autoInc = * returning id

}