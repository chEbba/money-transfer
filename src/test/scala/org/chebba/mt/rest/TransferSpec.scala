package org.chebba.mt.rest

import org.scalatest.{FlatSpec, Matchers}

/**
  * @author Kirill chEbba Chebunin
  */
class TransferSpec extends FlatSpec with Matchers with RestSpec {

  "Accounts" should "be empty on start but works" in {
    val resp = request(_ / "account")
    resp.getStatusCode should equal (200)
    resp.getContentType should equal ("application/json")
    resp.getResponseBody should equal ("[]")
  }
}
