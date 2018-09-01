package com.akkademy

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.util.Timeout
import org.scalatest.{FunSpec, Matchers}

import scala.concurrent.duration._

/**
  * Created by zhoudunxiong on 2018/9/1.
  */
class LastStrActorSpec extends FunSpec with Matchers {
  implicit val system = ActorSystem()
  implicit val timeout = Timeout(5.seconds)

  describe("lastStr") {
    val actorRef = TestActorRef(new LastStrActor)

    describe("given a string") {
      it("should place string") {
        actorRef ! "zdx"
        val lastStrActor = actorRef.underlyingActor
        lastStrActor.lastStr should be ("zdx")
      }
    }

    describe("given string twice") {
      it("should place last string") {
        actorRef ! "zdx"
        actorRef ! "ygy"
        val lastStrActor = actorRef.underlyingActor
        lastStrActor.lastStr should be ("ygy")
      }
    }
  }
}
