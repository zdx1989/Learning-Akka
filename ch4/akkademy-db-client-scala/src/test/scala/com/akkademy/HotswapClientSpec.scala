package com.akkademy

import akka.actor.{ActorSystem, Props, Status}
import akka.testkit.{TestActorRef, TestProbe}
import akka.util.Timeout
import com.akkademy.AkkademyDb.{Connected, SetRequest}
import org.scalatest.{FunSpec, Matchers}

import scala.concurrent.duration._

/**
  * Created by zhoudunxiong on 2018/9/6.
  */
class HotswapClientSpec extends FunSpec with Matchers {

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(5.seconds)

  describe("test hotswapClient") {
    it("should set a value into db") {
      val akkademyDbRef = TestActorRef(new AkkademyDb)
      val akkademyDb = akkademyDbRef.underlyingActor
      val probe = TestProbe()
      val client = TestActorRef(Props(classOf[HotswapClientActor], akkademyDbRef.path.toString))
      client ! SetRequest("zdx", 123, probe.ref)
      probe.expectMsg(Status.Success)
      akkademyDb.map.get("zdx") should be (Some(123))
    }
  }
}
