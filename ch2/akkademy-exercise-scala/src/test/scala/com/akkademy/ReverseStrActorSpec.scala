package com.akkademy

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask
import org.scalatest.{FunSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by zhoudunxiong on 2018/9/1.
  */
class ReverseStrActorSpec extends FunSpec with Matchers {
  val system = ActorSystem()
  implicit val timeout = Timeout(5.seconds)
  val reverseStrService = system.actorOf(Props[ReverseStrActor])

  describe("test ReverseStrActor") {
    describe("given a string") {
      it("should return a reverse string") {
        val future = reverseStrService ? "zdx"
        val result = Await.result(future.mapTo[String], 2.seconds)
        result should be ("xdz")
      }
    }

    describe("given a value of other type") {
      it("should throw ClassNotFindException") {
        val future = reverseStrService ? 123
        intercept[ClassNotFoundException] {
          Await.result(future.mapTo[Int], 2.seconds)
        }
      }
    }
  }
}
