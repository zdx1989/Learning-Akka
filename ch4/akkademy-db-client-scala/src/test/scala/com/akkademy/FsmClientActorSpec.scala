package com.akkademy

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestProbe}
import akka.util.Timeout
import com.akkademy.AkkademyDb.SetRequest
import com.akkademy.FSMClientActor.{Connected, ConnectedAndPending, DisConnected, Flush}
import org.scalatest.{FunSpec, Matchers}

import scala.concurrent.duration._

/**
  * Created by zhoudunxiong on 2018/9/9.
  */
class FsmClientActorSpec extends FunSpec with Matchers {
  implicit val system = ActorSystem()
  implicit val timeout = Timeout(5.seconds)

  val akkademyDbRef = TestActorRef(new AkkademyDb)
  val akkademyDb = akkademyDbRef.underlyingActor
  describe("test FSM") {
    val fsmClientRef = TestActorRef[FSMClientActor](Props(classOf[FSMClientActor], akkademyDbRef.path.toString))
    val fsmClient = fsmClientRef.underlyingActor
    it("should transform from DisConnected to ConnectedAndPending when getting a msg") {
      fsmClient.stateName should be (DisConnected)
      val testProbe = TestProbe()
      fsmClientRef ! SetRequest("zdx", 123, testProbe.ref)
      fsmClient.stateName should be (ConnectedAndPending)
      akkademyDb.map.get("zdx") should be (None)
    }

    it("should transform from ConnectedAndPending to Connected when getting a flush") {
      val testProbe = TestProbe()
      fsmClientRef ! SetRequest("zdx", 123, testProbe.ref)
      fsmClient.stateName should be (ConnectedAndPending)
      fsmClientRef ! Flush
      fsmClient.stateName should be (Connected)
      fsmClient.stateData should be (Nil)
      akkademyDb.map.get("zdx") should be (Some(123))
    }
  }


}
