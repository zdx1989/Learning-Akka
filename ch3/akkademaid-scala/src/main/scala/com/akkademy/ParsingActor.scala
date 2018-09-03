package com.akkademy

import akka.actor.Actor
import akka.event.Logging
import de.l3s.boilerpipe.extractors.ArticleExtractor

/**
  * Created by zhoudunxiong on 2018/9/3.
  */
class ParsingActor extends Actor {
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case ParseHtmlArticle(url, htmlString) =>
      val body = ArticleExtractor.INSTANCE.getText(htmlString)
      sender() ! ArticleBody(url, body)
    case o =>
      log.info(s"receive unknown message $o")
  }
}
