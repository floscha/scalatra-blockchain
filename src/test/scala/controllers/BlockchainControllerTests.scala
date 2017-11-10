package controllers

import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike

class BlockchainControllerTests extends ScalatraSuite with FunSuiteLike {

  addServlet(classOf[BlockchainController], "/*")

  test("GET / on BlockchainServlet should return status 200"){
    get("/"){
      status should equal (200)
    }
  }

}
