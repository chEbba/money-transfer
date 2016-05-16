package org.chebba.mt.http

import java.util.concurrent._
import java.util.concurrent.atomic._

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpResponseStatus
import org.slf4j.LoggerFactory

/**
  * @author Kirill chEbba Chebunin
  */
class SingleHttpStream(ctx: ChannelHandlerContext, timeout: Int, scheduler: ScheduledExecutorService) {
  val log = LoggerFactory.getLogger(getClass)

  val deadliner = new Runnable {
    def run() {
      if (scheduledWrite.compareAndSet(false, true)) {
        writeResponse(HttpResp(HttpResponseStatus.SERVICE_UNAVAILABLE))
      }
    }
  }
  
  var request: Option[HttpReq] = None
  val scheduledWrite = new AtomicBoolean(false)

  def startRequest(req: HttpReq) {
    request.fold {
      request = Some(req)
      scheduler.schedule(deadliner, timeout, TimeUnit.MILLISECONDS)
    } { old =>
      throw new IllegalStateException(s"Can not start ${req.id}. Single request ${old.id} was already started")
    }
  }

  def sendResponse(reqId: Long, resp: HttpResp) {
    request match {
      case Some(r) =>
        if (r.id != reqId || !scheduledWrite.compareAndSet(false, true)) {
        } else {
          writeResponse(resp)
        }
      case None =>
    }
  }

  def clear() {
    request = None
  }

  def stop() {
    clear()
  }

  def writeResponse(resp: HttpResp) {
    ctx.write(resp + Close)
    clear()
    ctx.flush()
    ctx.close()
  }
}
