package pong

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask
import org.scalatest.{FunSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by zhoudunxiong on 2018/9/1.
  */
class ScalaAskExampleSpec extends FunSpec with Matchers {
  val system = ActorSystem()
  implicit val timeout = Timeout(5.seconds)
  val pongActor = system.actorOf(Props[ScalaPongActor])

  describe("test pong actor") {
    it("should respond pong") {
      val future = pongActor ? "Ping"
      val result = Await.result(future.mapTo[String], 1.seconds)
      result should be ("Pong")
    }

    it("should failed on unknown message") {
      val future = pongActor ? "zdx"
      intercept[Exception] {
        Await.result(future.mapTo[String], 1.seconds)
      }
    }
  }
}
