package org.chebba.mt.rest

import org.chebba.mt.http.swagger.{RestController, RestRequest}
import org.chebba.mt.service.AccountService

import scala.concurrent.ExecutionContext

/**
  * @author Kirill chEbba Chebunin
  */
class AccountController(accountService: AccountService)(implicit protected val execctx: ExecutionContext) extends RestController {
  GET /"account" will "List all accounts" := { req: RestRequest[Unit] =>
    accountService.getAccounts()
  }
}
