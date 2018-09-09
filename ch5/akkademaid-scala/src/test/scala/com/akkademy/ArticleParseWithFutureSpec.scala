package com.akkademy

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/**
  * Created by zhoudunxiong on 2018/9/9.
  */
class ArticleParseWithFutureSpec extends FlatSpec with Matchers {
  import scala.concurrent.ExecutionContext.Implicits.global

  "ArticleParser" should "do work concurrently with future" in {
    val futures = (1 to 2000).map { i =>
      Future(ArticleParseActor.apply(TestHelper.file))
    }
    val fun = () => Await.ready(Future.sequence(futures), 30.seconds)
    TestHelper.profile(fun, "Futures")
  }
}
