package org.chebba.mt.rest

import io.netty.handler.codec.http.HttpResponseStatus._
import org.chebba.mt.http.path._
import org.chebba.mt.http.swagger.{ApiError, RestController, ApiRequest}
import org.chebba.mt.service.{AccountService, Profile}

import scala.concurrent.ExecutionContext

/**
  * @author Kirill chEbba Chebunin
  */
class AccountController(accountService: AccountService)(implicit protected val execctx: ExecutionContext) extends RestController {
  GET /"account" will "List all accounts" := { req: ApiRequest[Unit] =>
    accountService.getAccounts()
  }

  POST /"account" will "Create new Account" := { req: ApiRequest[Profile] =>
    accountService.addAccount(req.data)
  }

  GET /"account"/int("id") will "Get account by id" := { req: ApiRequest[Unit] =>
    val id = req.param[Int]("id")
    accountService.getAccount(id).map(_.getOrElse {
      throw new NotFoundException("account", id)
    })
  } errors {
    NOT_FOUND -> "Account is not found"
  }
}


class NotFoundException(resource: String, id: Any) extends ApiError(NOT_FOUND, s"Resource $resource#$id is not found")
