package com.akkademy

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.RoundRobinPool
import com.akkademy.ArticleParseActor.ParseArticle
import com.akkademy.TestHelper.TestCameoActor
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration._

/**
  * Created by zhoudunxiong on 2018/9/9.
  */
class ArticleParseWithActorSpec extends FlatSpec with Matchers {
  val system = ActorSystem()

  val workRouter: ActorRef =
    system.actorOf(
      Props.create(classOf[ArticleParseActor])
        .withDispatcher("my-dispatcher")
        .withRouter(new RoundRobinPool(8)), "workerRouter")
  val future = Future(1)(system.dispatcher)

  "ArticleParseActor" should "work in concurrently" in {
    val p = Promise[String]()
    val cameoActor: ActorRef = system.actorOf(Props(classOf[TestCameoActor], p))
    (0 to 2000).foreach { i =>
      workRouter.tell(ParseArticle(TestHelper.file), cameoActor)
    }
    TestHelper.profile(() => Await.ready(p.future, 20.seconds), "Actor")
  }
}
