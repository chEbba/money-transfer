package org.chebba.mt.service

import org.chebba.mt.util.Money

import scala.concurrent.Future

/**
  * @author Kirill chEbba Chebunin
  */
trait TransactionService {
  def creditAccount(accountId: Int, amount: Money): Future[CreditTransaction]
  def transferToAccount(fromAccountId: Int, toAccountId: Int, amount: Money): Future[(TransferFromTransaction, TransferToTransaction)]
  def getTransactions(accountId: Int): Future[Seq[Transaction]]
}

sealed trait Transaction {
  def info: TransactionInfo
}

case class TransactionInfo(id: Int, accountId: Int, amount: Money, time: Long, status: TransactionStatus)

case class CreditTransaction(info: TransactionInfo) extends Transaction
case class TransferFromTransaction(info: TransactionInfo, toAccount: Int) extends Transaction
case class TransferToTransaction(info: TransactionInfo, fromAccount: Int, fromTransaction: Int) extends Transaction

sealed trait TransactionStatus

object TransactionStatus {
  case object HOLD      extends TransactionStatus
  case object COMPLETE  extends TransactionStatus
  case object ERROR     extends TransactionStatus
}
