package models

case class Block(index: Int,
                 timestamp: Long,
                 transactions: List[Transaction],
                 proof: Int,
                 previousHash: String)