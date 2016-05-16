package org.chebba.mt.util

import com.typesafe.scalalogging.Logger

/**
  * @author Kirill chEbba Chebunin
  */
trait Loggable {
  implicit protected val log = Logger(getClass)
}
