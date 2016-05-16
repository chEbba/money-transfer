package org.chebba.mt.rest

import org.chebba.mt.service.{Account, Profile}
import org.chebba.mt.util.Money
import play.api.libs.json._

/**
  * @author Kirill chEbba Chebunin
  */
trait JsonFormat {
  implicit val unitFormat = Format[Unit](
    Reads(_ => JsSuccess(Unit)),
    Writes(_ => JsNull)
  )

  implicit val moneyFormat = Format[Money](
    Reads {
      case JsNumber(d) => JsSuccess(Money(d))
      case unexpected => JsError(s"$unexpected is not a Number")
    },
    Writes { money =>
      JsNumber(money.toDecimal)
    }
  )
  implicit val profileFormat = Json.format[Profile]
  implicit val accountFormat = Json.format[Account]
}
