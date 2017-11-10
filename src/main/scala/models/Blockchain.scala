package models

import com.roundeights.hasher.Implicits._

import scalaj.http._
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.annotation.tailrec

case class ChainResponse(chain: List[Block], length: Int)

object Blockchain {
  var chain: List[Block] = List.empty
  var currentTransactions: List[Transaction] = List.empty

  var nodes: Set[String] = Set()

  implicit val formats: Formats = DefaultFormats // Brings in default date formats etc.

  // Create the genesis block.
  newBlock(100, "1")

  /**
    * Hashes a block.
    *
    * @param block Block.
    * @return The hashed Block.
    */
  def hash(block: Block): String = {
    val blockString = s"$block.index $block.timestamp $block.transactions $block.proof $block.previousHash"
    val hashedBlock = blockString.sha256.hex
    hashedBlock
  }

  /**
    * Creates a new Block and adds it to the chain.
    *
    * @param proof        The proof given by the Proof of Work algorithm.
    * @param previousHash Hash of previous Block.
    * @return New Block.
    */
  def newBlock(proof: Int, previousHash: String = null): Block = {
    val block = Block(
      index = chain.length + 1,
      timestamp = System.currentTimeMillis / 1000,
      transactions = currentTransactions,
      proof = proof,
      previousHash = if (previousHash != null) previousHash else Blockchain.hash(chain.last)
    )

    // Reset the current list of transactions.
    currentTransactions = List.empty

    chain = chain :+ block
    block
  }

  /**
    * Add a transaction to go into the next mined Block.
    *
    * @param transaction Transaction to be added.
    * @return The index of the Block that will hold this transaction.
    */
  def newTransaction(transaction: Transaction): Int = {
    currentTransactions = currentTransactions :+ transaction

    lastBlock.index + 1
  }

  /**
    * Creates a new transaction to go into the next mined Block.
    *
    * @param sender    Address of the sender.
    * @param recipient Address of the recipient.
    * @param amount    Amount.
    * @return The index of the Block that will hold this transaction.
    */
  def newTransaction(sender: String, recipient: String, amount: Int): Int = {
    newTransaction(Transaction(sender, recipient, amount))
  }

  /**
    * Returns the last block in the chain.
    */
  def lastBlock: Block = {
    chain.last
  }

  /**
    * Add a new node to the list of nodes.
    *
    * @param address Address of node. Eg. 'http://192.168.0.5:5000'.
    * @return
    */
  def registerNode(address: String): Unit = {
    nodes = nodes + address
  }

  /**
    * Determine if a given blockchain is valid.
    *
    * @param chain A blockchain.
    * @return True if chain is valid, false otherwise.
    */
  @tailrec
  def validChain(chain: List[Block]): Boolean = {
    chain match {
      case x :: y :: tail =>
        // Check that the hash of the block is correct.
        if (y.previousHash != Blockchain.hash(x))
          false
        // Check that the Proof of Work is correct.
        else if (!ProofOfWork.validateProof(x.proof, y.proof))
          false
        else validChain(y :: tail)
      case _ => true
    }
  }

  /**
    * This is our Consensus Algorithm, it resolves conflicts
    * by replacing our chain with the longest one in the network.
    *
    * @return True if our chain was replaced, false otherwise.
    */
  def resolveConflicts: Boolean = {
    var newChain: Option[List[Block]] = None

    // We're only looking for chains longer than ours.
    var maxLength: Int = chain.length

    // Grab and verify the chains from all the nodes in our network.
    // Synchronous calls are potentially slow but serve their purpose for this easy example.
    for (node: String <- nodes) {
      val response: HttpResponse[String] = Http(s"$node/chain").asString
      println(node)
      println(response)

      if (response.isSuccess) {
        val jsonData = parse(response.body)
        val chainResponse = jsonData.extract[ChainResponse]
        println(chainResponse.length)

        // Check if the length is longer and the chain is valid.
        if (chainResponse.length > maxLength && validChain(chainResponse.chain)) {
          maxLength = chainResponse.length
          newChain = Some(chainResponse.chain)
        }
      }
    }

    newChain match {
      case Some(c) =>
        chain = c
        true
      case None =>
        false
    }
  }
}
