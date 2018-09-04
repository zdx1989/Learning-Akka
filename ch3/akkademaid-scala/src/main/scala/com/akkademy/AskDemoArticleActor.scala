package com.akkademy

import akka.actor.{Actor, Status}
import akka.event.Logging
import akka.util.Timeout
import akka.pattern.ask
import com.akkademy.AkkademyDb.{GetRequest, SetRequest}

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by zhoudunxiong on 2018/9/3.
  */
class AskDemoArticleActor(cacheActorPath: String,
                          httpClientActorPath: String,
                          articleParserActorPath: String,
                          implicit val timeout: Timeout) extends Actor {
  val cacheActor = context.system.actorSelection(cacheActorPath)
  val httpClientActor = context.actorSelection(httpClientActorPath)
  val articleParserActor = context.actorSelection(articleParserActorPath)
  val log = Logging(context.system, this)
  import scala.concurrent.ExecutionContext.Implicits.global

  override def receive: Receive = {
    case ParseArticle(url) =>
      val senderRef = sender()//重点排查
      val cacheResult = cacheActor ? GetRequest(url)
      val result = cacheResult.recoverWith {
        case _: Exception =>
          val resp = httpClientActor ? url
          resp.flatMap {
            case HttpResponse(body) =>
              cacheActor ! SetRequest(url, body)
              articleParserActor ? ParseHtmlArticle(url, body)
            case o =>
              Future.failed(new Exception(s"unknown message: $o"))
          }
      }
      result.onComplete {
        case Success(body: String) =>
          senderRef ! body
        case Success(ArticleBody(_, body)) =>
          cacheActor ! SetRequest(url, body)
          senderRef ! body
        case Failure(t) =>
          senderRef ! Status.Failure(t)
        case o =>
          log.info(s"receive unknown message: $o")
      }
  }
}
