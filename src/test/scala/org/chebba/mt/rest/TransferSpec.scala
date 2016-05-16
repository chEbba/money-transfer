package org.chebba.mt.rest

import com.ning.http.client.Response
import org.scalatest.FlatSpec
import play.api.libs.json._

import scala.reflect.Manifest

/**
  * @author Kirill chEbba Chebunin
  */
class TransferSpec extends FlatSpec with RestSpec {

  "Account" should "have empty list on start but works" in {
    val resp = request(_ / "account")
    resp.getStatusCode should equal (200)
    resp.getContentType should equal ("application/json")
    resp.getResponseBody should equal ("[]")
  }

  it should "be created with post" in {
    val resp = request(r => (r / "account").POST << """{"name": "test1"}""")
    resp.getStatusCode should equal (200)

    //{"id":1,"profile":{"name":"test1"},"balance":0}
    val acc = json[JsObject](resp)
    checkAccount(acc, 1, "test1", 0)
  }

  it should "have 1 item" in {
    val resp = request(_ / "account")
    resp.getStatusCode should equal (200)

    val accs = json[JsArray](resp)
    accs.value.size should equal (1)

    val acc = accs.value.head.asInstanceOf[JsObject]
    checkAccount(acc, 1, "test1", 0)
  }

  it should "have 2 items" in {
    val add = request(r => (r / "account").POST << """{"name": "test2"}""")
    add.getStatusCode should equal (200)

    val list = request(_ / "account")
    list.getStatusCode should equal (200)

    val accs = json[JsArray](list)
    accs.value.size should equal (2)

    checkAccount(accs.value(0).asInstanceOf[JsObject], 1, "test1", 0)
    checkAccount(accs.value(1).asInstanceOf[JsObject], 2, "test2", 0)
  }

  it should "be found by id" in {
    val resp = request(_ / "account" / "1")
    resp.getStatusCode should equal (200)
    val acc = json[JsObject](resp)
    checkAccount(acc, 1, "test1", 0)
  }

  def json[T <: JsValue : Manifest](resp: Response): T = {
    val js = Json.parse(resp.getResponseBody)
    js shouldBe a [T]
    js.asInstanceOf[T]
  }

  def checkAccount(obj: JsObject, id: Int, name: String, balance: BigDecimal): Unit = {
    obj.value("id") should equal (JsNumber(id))
    obj.value("balance") should equal (JsNumber(balance))
    val profile = obj.value("profile").asInstanceOf[JsObject]
    profile.value("name") should equal (JsString(name))
  }
}
