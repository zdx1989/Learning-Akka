package com.akkademy

import akka.actor.{ActorSystem, Props}
import akka.routing.RoundRobinGroup
import com.akkademy.ArticleParseActor.ParseArticle
import com.akkademy.TestHelper.TestCameoActor
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.{Await, Promise}
import scala.concurrent.duration._

/**
  * Created by zhoudunxiong on 2018/9/12.
  */
class AssignActorToDispatcher extends FlatSpec with Matchers {
  val system = ActorSystem()

  "AssignActorToDispatcher" should "work concurrently" in {
    val p = Promise[String]()
    val actors = (0 to 7).map { _ =>
      system.actorOf(Props[ArticleParseActor]
          .withDispatcher("article-parsing-dispatcher")
      )
    }

    val workRoute = system.actorOf(
      RoundRobinGroup(
        actors.map(_.path.toStringWithoutAddress)).props(),
      "workerRouter"
    )

    val cameoActor = system.actorOf(Props(new TestCameoActor(p)))

    (0 to 2000).foreach { _ =>
      workRoute.tell(ParseArticle(TestHelper.file), cameoActor)
    }

    TestHelper.profile(() => Await.ready(p.future, 20.seconds), "AssignActorToDispatcher")
  }

}
