package models

import com.roundeights.hasher.Implicits._

import scala.annotation.tailrec
import scala.language.postfixOps

object ProofOfWork {

  /**
    * Simple Proof of Work algorithm:
    * - Find a number p' such that hash(pp') contains leading 4 zeroes, where p is the previous p'
    * - p is the previous proof, and p' is the new proof
    *
    * @param lastProof Previous proof.
    * @return A new valid proof.
    */
  def findP(lastProof: Int): Int = {
    @tailrec
    def loop(lastProof: Int, proofCandidate: Int): Int = {
      // Guess values for proof until a valid one is found.
      if (validateProof(lastProof, proofCandidate)) proofCandidate
      else loop(lastProof, proofCandidate + 1)
    }
    loop(lastProof, 0)
  }

  /**
    * Validates the Proof: Does hash(last_proof, proof) contain 4 leading zeroes?
    *
    * @param lastProof Previous proof.
    * @param proof     Current proof.
    * @return True if proof is valid, false otherwise.
    */
  def validateProof(lastProof: Int, proof: Int): Boolean = {
    val guess: String = lastProof * proof toString
    val guessHash = guess.sha256.hex
    val isGuessValid = guessHash.take(4) == "0000"
    isGuessValid
  }

}
