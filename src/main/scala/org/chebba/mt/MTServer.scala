package org.chebba.mt

import org.chebba.mt.http.swagger.RestHandler
import org.chebba.mt.http.{HttpServer, HttpServerOptions}
import org.chebba.mt.rest.AccountController
import org.chebba.mt.service.memory.MemoryTransferService
import org.chebba.mt.util.{Loggable, ThreadPool}

import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

/**
  * @author Kirill chEbba Chebunin
  */
object MTServerApp extends App with Loggable {

  val port = property("port")(_.toInt, 9000)

  MTServer(port = port).start()

  private def property[A](name: String)(convert: String => A, default: A): A = {
    val property = s"${getClass.getPackage.getName}.$name"
    sys.props.get(property).flatMap { p =>
      try {
        Some(convert(p))
      } catch {
        case NonFatal(e) =>
          log.warn(s"Can not convert property '$property'", e)
          None
      }
    }.getOrElse(default)
  }
}

object MTServer {

  def apply(port: Int) = {
    val servicePool: ExecutionContext = ThreadPool.fixedPool()
    val restPool: ExecutionContext = ThreadPool.fixedPool()

    val memoryService = new MemoryTransferService()(servicePool)

    val accountController = new AccountController(memoryService, memoryService)(restPool)

    new HttpServer(handler = RestHandler(Seq(accountController)), options = HttpServerOptions(
      port = port,
      maxContentLength = 1024*1024
    ))
  }
}
