package com.akkademy

import akka.actor.{ActorSystem, Status}
import akka.testkit.{TestActorRef, TestProbe}
import akka.pattern.ask
import akka.util.Timeout
import com.akkademy.AkkademyDb.{GetRequest, SetRequest}
import org.scalatest.{FunSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by zhoudunxiong on 2018/9/4.
  */
class AkkademyDbSpec extends FunSpec with Matchers {
  implicit val system = ActorSystem()
  implicit val timeout = Timeout(5.seconds)

  describe("test akkademyDb") {
    val akkademyDbRef = TestActorRef(new AkkademyDb)
    val akkademyDb = akkademyDbRef.underlyingActor

    it("should set a value") {
      val probe = TestProbe()
      akkademyDbRef ! SetRequest("zdx", 123, probe.ref)
      probe.expectMsg(Status.Success)
      akkademyDb.map.get("zdx") should be (Some(123))
    }

    it("should get a value") {
      akkademyDb.map.put("zdx", 456)
      val future = akkademyDbRef ? GetRequest("zdx")
      val result = Await.result(future.mapTo[Int], 2.seconds)
      result should be (456)
    }

    it("should set list value") {
      akkademyDbRef ! List(
        SetRequest("zdx", 123),
        SetRequest("ygy", 456)
      )
      akkademyDb.map.get("zdx") should be (Some(123))
      akkademyDb.map.get("ygy") should be (Some(456))
    }
  }
}
