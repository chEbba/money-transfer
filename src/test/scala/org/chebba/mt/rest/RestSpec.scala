package org.chebba.mt.rest

import dispatch._
import org.chebba.mt.MTServer
import org.chebba.mt.util.ThreadPool
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * @author Kirill chEbba Chebunin
  */
trait RestSpec extends BeforeAndAfterAll with Matchers { this: Suite =>
  implicit private val exectx = ThreadPool.fixedPool()

  private val server = MTServer(port = 9000)
  protected val baseUrl = url("http://localhost:9000/")

  override def beforeAll() = {
    server.start()
  }

  override def afterAll() = {
    server.shutdown()
  }

  def request(builder: Req => Req, duration: Duration = 5 seconds) = {
    Await.result(Http(builder(baseUrl)), duration)
  }

  def inTime[X](f: => X)(time: X => Long) = {
    val start = System.currentTimeMillis()
    val res = f
    val end = System.currentTimeMillis()
    time(res) should (be >= start and be <= end)
  }
}
