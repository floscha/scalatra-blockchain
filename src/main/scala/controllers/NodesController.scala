package controllers

import models._
import org.json4s.{DefaultFormats, Formats, JValue}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{BadRequest, Ok, Created}

case class RegisterNodeRequest(nodes: List[String])

case class RegisterNodeResponse(message: String, totalNodes: Set[String])

case class ResolveConflictsResponse(message: String, chain: List[Block])

class NodesController extends ScalatraServlet with JacksonJsonSupport {

  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  // Turn camel case keys to underscore.
  protected override def transformResponseBody(body: JValue): JValue = body.underscoreKeys

  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }

  post("/register") {
    val nodes = parsedBody.extract[RegisterNodeRequest].nodes

    if (nodes == null) {
      BadRequest("Error: Please supply a valid list of nodes")
    }

    for (node: String <- nodes) {
      Blockchain.registerNode(node)
    }

    val response = RegisterNodeResponse("New nodes have been added", Blockchain.nodes)
    Created(response)
  }

  get("/resolve") {
    val replaced: Boolean = Blockchain.resolveConflicts

    val response = {
      if (replaced)
        ResolveConflictsResponse("Our chain was replaced", Blockchain.chain)
      else
        ResolveConflictsResponse("Our chain is authoritative", Blockchain.chain)
    }
    Ok(response)
  }

}