package com.akkademy

import akka.actor.Actor
import de.l3s.boilerpipe.extractors.ArticleExtractor

/**
  * Created by zhoudunxiong on 2018/9/28.
  */
class ArticleParseActor extends Actor {

  override def receive: Receive = {
    case htmlString: String =>
      val body = ArticleParseActor(htmlString)
      sender() ! body
    case _ => println("msg!")
  }
}

object ArticleParseActor {

  def apply(htmlString: String): String =
    ArticleExtractor.INSTANCE.getText(htmlString)
}
