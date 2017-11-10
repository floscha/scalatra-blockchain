package controllers

import org.scalatra.ScalatraServlet
import org.scalatra.{Ok, Accepted, Created}
import org.scalatra.json._
import org.json4s.{DefaultFormats, Formats}
import models._

case class ChainResponse(chain: List[Block], length: Int)

case class MiningResponse(message: String, block: Block = null)

case class NewTransactionResponse(message: String)

class BlockchainController extends ScalatraServlet with JacksonJsonSupport {

  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }

  // Create a unique ID for the blockchain.
  val nodeIdentifier: String = java.util.UUID.randomUUID.toString.replace("-", "")

  get("/chain") {
    val response = ChainResponse(Blockchain.chain, Blockchain.chain.length)
    println(Blockchain.chain.length)
    Ok(response)
  }

  get("/mine") {
    // We run the proof of work algorithm to get the next proof...
    val lastBlock: Block = Blockchain.lastBlock
    val lastProof: Int = lastBlock.proof
    val proof: Int = ProofOfWork.findP(lastProof)

    // We must receive a reward for finding the proof.
    // The sender is "0" to signify that this node has mined a new coin.
    Blockchain.newTransaction(
      sender = "0",
      recipient = nodeIdentifier,
      amount = 1
    )

    // Forge the new Block by adding it to the chain.
    val block: Block = Blockchain.newBlock(proof)

    val response = MiningResponse("New block forged", block)
    Accepted(response)
  }

  post("/transactions/new") {
    val transaction = parsedBody.extract[Transaction]

    // Add the transaction to the current Block.
    val index: Int = Blockchain.newTransaction(transaction)

    val response = NewTransactionResponse(s"Transaction will be added to Block $index")
    Created(response)
  }
}
