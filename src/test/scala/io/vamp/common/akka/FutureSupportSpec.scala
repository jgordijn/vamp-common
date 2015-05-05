package io.vamp.common.akka

import java.util
import java.util.Collections

import org.scalatest.concurrent.{ Futures, ScalaFutures }
import org.scalatest.{ FunSuite, Matchers }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FutureSupportSpec extends FunSuite with Matchers with Futures with ScalaFutures {

  test("FutureSuppport.sequentialExecution does not do Sequential execution as the name implies.") {
    object support extends FutureSupport
    val orderOfExecution = Collections.synchronizedList(new util.ArrayList[String]())

    val future2 = Future {
      orderOfExecution.add("Future2")
      "Future2"
    }
    val future1 = Future {
      // Needed to force that this future will complete somewhat later
      Thread.sleep(10)
      orderOfExecution.add("Future1")
      "Future1"
    }

    val result = support.sequentialExecution(List(future1, future2))

    println(result)
    result.futureValue should be(List("Future1", "Future2"))
    orderOfExecution.get(0) should be("Future1")
    orderOfExecution.get(1) should be("Future2")
  }

}
