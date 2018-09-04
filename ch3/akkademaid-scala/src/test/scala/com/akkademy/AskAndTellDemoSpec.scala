package com.akkademy

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestProbe}
import akka.util.Timeout
import akka.pattern.ask
import de.l3s.boilerpipe.extractors.ArticleExtractor
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
    val cacheActorPth = "akka.tcp://akkademy@127.0.0.1:2552/user/akkademy-db"
    val httpClientProbe = TestProbe()
    val articleParseActorRef = system.actorOf(Props[ParsingActor])
    val askActor = system.actorOf(
      Props(
      classOf[AskDemoArticleActor],
      cacheActorPth,
      httpClientProbe.ref.path.toString,
      articleParseActorRef.path.toString,
      timeout
    ))

    it("should provided parsed article") {
      val future = askActor ? ParseArticle("http://www.baidu.com")
      httpClientProbe.expectMsgType[String]
      httpClientProbe.reply(HttpResponse(Articles.article1))

      val result = Await.result(future.mapTo[String], 10.seconds)
      result should include ("I’ve been writing a lot in emacs lately")
      result should not include ("<body>")
    }

    it("should provided cached article") {
      val cacheActorRef = TestActorRef(new AkkademyDb)
      val cacheActor = cacheActorRef.underlyingActor
      val body = ArticleExtractor.INSTANCE.getText(Articles.article1)
      cacheActor.map.put("http://www.baidu.com", body)
      val future = askActor ? ParseArticle("http://www.baidu.com")

      val result = Await.result(future.mapTo[String], 10.seconds)
      result should include ("I’ve been writing a lot in emacs lately")
      result should not include ("<body>")

    }
  }

  describe("tell demo") {

    val cacheActorPth = "akka.tcp://akkademy@127.0.0.1:2552/user/akkademy-db"
    val httpClientProbe = TestProbe()
    val articleParseActorRef = system.actorOf(Props[ParsingActor])
    val tellActor = system.actorOf(
      Props(
        classOf[TellDemoArticleActor],
        cacheActorPth,
        httpClientProbe.ref.path.toString,
        articleParseActorRef.path.toString,
        timeout
      ))
    it("should provided parsed article") {
      val future = tellActor ? ParseArticle("http://www.baidu.com")
      httpClientProbe.expectMsgType[String]
      httpClientProbe.reply(HttpResponse(Articles.article1))

      val result = Await.result(future.mapTo[String], 10.seconds)
      result should include ("I’ve been writing a lot in emacs lately")
      result should not include ("<body>")
    }

    it("should provided cached article") {
      val cacheActorRef = TestActorRef(new AkkademyDb)
      val cacheActor = cacheActorRef.underlyingActor
      val body = ArticleExtractor.INSTANCE.getText(Articles.article1)
      cacheActor.map.put("http://www.baidu.com", body)
      val future = tellActor ? ParseArticle("http://www.baidu.com")

      val result = Await.result(future.mapTo[String], 10.seconds)
      result should include ("I’ve been writing a lot in emacs lately")
      result should not include ("<body>")

    }
  }
}
