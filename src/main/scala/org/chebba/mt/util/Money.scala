package org.chebba.mt.util

import scala.math.BigDecimal.RoundingMode

/**
  * @author Kirill chEbba Chebunin
  */
case class Money(value: Long) extends AnyVal {
  def toDecimal = BigDecimal(value) / 100
}
object Money {
  def apply(decimal: BigDecimal): Money = new Money(decimal.setScale(2, RoundingMode.DOWN) * 100 toLong)
}
