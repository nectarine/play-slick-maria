package controllers

import play.api.mvc.{ Action, Controller }
import play.api.db.DB
import play.api.Play.current
import scala.slick.driver.MySQLDriver.simple._
import models._
import models.Student
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json

object StudentController extends Controller {

  lazy val database = Database.forDataSource(DB.getDataSource())
  implicit val studentWrite = Json.writes[Student]

  val creationForm = Form(
    mapping(
      "id" -> optional(number),
      "name" -> nonEmptyText,
      "class" -> optional(number))(Student.apply)(Student.unapply))

  def create = Action {
    implicit request =>
      val body = request.body
      val studentName = body.asFormUrlEncoded.get("name")(0)
      val classId = body.asFormUrlEncoded.get("class")(0)

      database.withSession {
        implicit s: Session =>
          Students.autoInc.insert(new Student(None, studentName, Some(classId.toInt)))
      }

      Ok(studentName + " created in " + classId)
  }

  def createWithForm = Action {
    implicit request =>
      creationForm.bindFromRequest.fold(
        formWithErrors => BadRequest(formWithErrors.toString),
        student => {
          database.withSession {
            implicit s: Session =>
              Students.autoInc.insert(new Student(student.id, student.name, student.classId))
          }
          Ok(student.name + " created or updated in " + student.classId)
        })

  }

  def findByClassName(className: String) = Action {

    implicit request =>
      database.withSession {
        implicit s: Session =>
          {
            val queried = for {
              c <- Classes if c.name === className
              s <- Students if c.id === s.classId
            } yield (s)

            Ok(Json.toJson(queried.list))
          }
      }
  }

}