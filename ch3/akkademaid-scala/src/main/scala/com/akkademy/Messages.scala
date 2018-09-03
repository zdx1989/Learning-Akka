package com.akkademy

/**
  * Created by zhoudunxiong on 2018/9/3.
  */

case class ParseArticle(url: String)

case class ParseHtmlArticle(url: String, htmlString: String)

case class HttpResponse(body: String)

case class ArticleBody(url: String, body: String)
