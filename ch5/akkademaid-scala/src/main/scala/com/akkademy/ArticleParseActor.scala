package com.akkademy

import akka.actor.Actor
import com.akkademy.ArticleParseActor.ParseArticle
import de.l3s.boilerpipe.extractors.ArticleExtractor

/**
  * Created by zhoudunxiong on 2018/9/9.
  */
class ArticleParseActor extends Actor {
  override def receive: Receive = {
    case ParseArticle(html) =>
      sender() ! ArticleParseActor(html)
  }
}

object ArticleParseActor {
  def apply(htmlStr: String): String =
    ArticleExtractor.INSTANCE.getText(htmlStr)

  case class ParseArticle(html: String)
}