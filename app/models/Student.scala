package models

import scala.slick.driver.MySQLDriver.Table

object Students extends Table[(Int, String, Int)]("students"){
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc) 
    def name = column[String]("name")
    def clazz = column[Int]("class")
    

}