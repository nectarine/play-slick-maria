package controllers

import play.api.mvc.{ Action, Controller }
import play.api.libs.json.Json
import play.api.db.DB
import play.api.Play.current
import models.Classes
import models.Class
import scala.slick.driver.MySQLDriver.simple._

object ClassController extends Controller {

  lazy val database = Database.forDataSource(DB.getDataSource())
  implicit val classWrite = Json.writes[Class]

  def classHello = Action {
    Ok("Hello Class!")
  }

  def create = Action {
    implicit request =>
      val body = request.body
      val className = body.asFormUrlEncoded.get("class")(0)

      database.withSession {
        implicit session: Session =>
          Classes.add(className)
      }

      Ok(className + " created")
  }

  def all = Action {
    database.withSession {
      implicit session: Session =>
        Ok(Json.toJson(Query(Classes).list))
    }
  }

}