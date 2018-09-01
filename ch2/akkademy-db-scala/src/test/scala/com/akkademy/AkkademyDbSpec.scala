package com.akkademy

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestActorRef
import akka.util.Timeout
import akka.pattern.ask
import com.akkademy.AkkademyDb.{GetRequest, SetRequest}
import org.scalatest.{FunSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by zhoudunxiong on 2018/9/1.
  */
class AkkademyDbSpec extends FunSpec with Matchers {
  implicit val system = ActorSystem()
  implicit val timeout = Timeout(5.seconds)

  describe("test akkademyDb") {
    describe("given SetRequest") {
      it("should place key/value into map") {
        val actorRef = TestActorRef(new AkkademyDb)
        actorRef ! SetRequest("zdx", 123)
        val akkademyDb = actorRef.underlyingActor
        akkademyDb.map.get("zdx") should be (Some(123))
      }
    }

    describe("given GetRequest") {
      it("should respond value of key") {
        val actorRef = system.actorOf(Props[AkkademyDb])
        actorRef ! SetRequest("zdx", 123)
        val future = actorRef ? GetRequest("zdx")
        val result = Await.result(future, 2.seconds)
        result should be (123)
      }
    }
  }
}
