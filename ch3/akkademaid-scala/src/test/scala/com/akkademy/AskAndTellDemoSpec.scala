package com.akkademy

import akka.actor.Status.Failure
import akka.actor.{ActorSystem, Props}
import akka.testkit.TestProbe
import akka.util.Timeout
import akka.pattern.ask
import com.akkademy.AkkademyDb.{GetRequest, SetRequest}
import org.scalatest.{FunSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by zhoudunxiong on 2018/9/3.
  */
class AskAndTellDemoSpec extends FunSpec with Matchers {
  implicit val system = ActorSystem("test")
  implicit val timeout = Timeout(10.seconds)

  describe("ask demo") {
    val cacheProbe = TestProbe()
    val httpClientProbe = TestProbe()
    val articleParseActorRef = system.actorOf(Props[ParsingActor])
    val askActor = system.actorOf(
      Props(
      classOf[AskDemoArticleActor],
      cacheProbe.ref.path.toString,
      httpClientProbe.ref.path.toString,
      articleParseActorRef.path.toString,
      timeout
    ))

    it("should provided parsed article") {
      val future = askActor ? ParseArticle("http://www.baidu.com")
      cacheProbe.expectMsgType[GetRequest]
      cacheProbe.reply(Failure(new Exception("no cache")))
      httpClientProbe.expectMsgType[String]
      httpClientProbe.reply(HttpResponse(Articles.article1))
      cacheProbe.expectMsgType[SetRequest]

      val result = Await.result(future.mapTo[String], 10.seconds)
      result should include ("I’ve been writing a lot in emacs lately")
      result should not include ("<body>")
    }
  }
}
