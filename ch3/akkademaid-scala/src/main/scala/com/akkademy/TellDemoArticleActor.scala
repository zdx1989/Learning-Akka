package com.akkademy

import java.util.concurrent.TimeoutException

import akka.actor.{Actor, ActorRef, Props, Status}
import akka.event.Logging
import akka.util.Timeout
import com.akkademy.AkkademyDb.{GetRequest, SetRequest}

import scala.concurrent.duration._

/**
  * Created by zhoudunxiong on 2018/9/3.
  */
class TellDemoArticleActor(cacheActorPath: String,
                           httpClientActorPath: String,
                           articleParserActorPath: String,
                           implicit val timeout: Timeout) extends Actor {
  val cacheActor = context.actorSelection(cacheActorPath)
  val httpClientActor = context.actorSelection(httpClientActorPath)
  val articleParserActor = context.actorSelection(articleParserActorPath)
  val log = Logging(context.system, this)
  import scala.concurrent.ExecutionContext.Implicits.global

  override def receive: Receive = {
    case ParseArticle(url) =>
      val extraActor = buildExtraActor(sender(), url)
      cacheActor.tell(GetRequest(url), extraActor)
      httpClientActor.tell(url, extraActor)
      context.system.scheduler.scheduleOnce(3.seconds, extraActor, "timeout")
  }

  def buildExtraActor(senderRef: ActorRef, url: String): ActorRef = {
    context.actorOf(Props(new Actor {
      override def receive: Receive = {
        case "timeout" =>
          senderRef ! Status.Failure(new TimeoutException("timeout"))
        case body: String =>
          senderRef ! body
          context.stop(self)
        case ArticleBody(_, body) =>
          cacheActor ! SetRequest(url, body)
          senderRef ! body
          context.stop(self)
        case HttpResponse(body) =>
          articleParserActor ! ParseHtmlArticle(url, body)
        case o =>
            log.info(s"receive unknown message: $o")
      }
    }))
  }
}
