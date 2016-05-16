package org.chebba.mt.util

import java.util.concurrent.Executors

import scala.concurrent.ExecutionContext

/**
  * @author Kirill chEbba Chebunin
  */
object ThreadPool {
  def fixedPool(threads: Int = Runtime.getRuntime.availableProcessors()): ExecutionContext = {
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(threads))
  }
}
