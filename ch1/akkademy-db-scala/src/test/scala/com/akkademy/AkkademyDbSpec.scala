package com.akkademy

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.util.Timeout
import com.akkademy.AkkademyDb.SetRequest
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.duration._

/**
  * Created by zhoudunxiong on 2018/9/1.
  */
class AkkademyDbSpec extends FunSpecLike with Matchers {
  implicit val system = ActorSystem()
  implicit val timeout = Timeout(5.seconds)

  describe("akkademyDb") {
    describe("given SetRequest") {
      it("should place key/value into map") {
        val actorRef = TestActorRef(new AkkademyDb)
        actorRef ! SetRequest("zdx", 29)
        val akkademyyDb = actorRef.underlyingActor
        akkademyyDb.map.get("zdx") should be (Some(29))
      }
    }
  }
}
