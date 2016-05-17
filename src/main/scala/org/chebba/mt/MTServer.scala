package org.chebba.mt

import org.chebba.mt.http.swagger.RestHandler
import org.chebba.mt.http.{HttpServer, HttpServerOptions}
import org.chebba.mt.rest.AccountController
import org.chebba.mt.service.memory.MemoryTransferService
import org.chebba.mt.util.ThreadPool

import scala.concurrent.ExecutionContext

/**
  * @author Kirill chEbba Chebunin
  */
object MTServerApp extends App {
  MTServer(port = 9000).start()
}

object MTServer {

  def apply(port: Int) = {
    val servicePool: ExecutionContext = ThreadPool.fixedPool()
    val restPool: ExecutionContext = ThreadPool.fixedPool()

    val memoryService = new MemoryTransferService()(servicePool)

    val accountController = new AccountController(memoryService, memoryService)(restPool)

    new HttpServer(handler = RestHandler(Seq(accountController)), options = HttpServerOptions(
      port = port
    ))
  }
}
