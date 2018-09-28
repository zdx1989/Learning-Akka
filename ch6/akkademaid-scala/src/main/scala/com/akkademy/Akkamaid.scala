package com.akkademy

import akka.actor.{ActorSystem, Props}
import akka.contrib.pattern.ClusterReceptionistExtension
import akka.routing.BalancingPool

/**
  * Created by zhoudunxiong on 2018/9/26.
  */
object Akkamaid extends App {
  val system = ActorSystem("Akkademy")
  val clusterController = system.actorOf(Props[ClusterController], "clusterController")

  val workers = system.actorOf(
    BalancingPool(5).props(Props[ArticleParseActor]),
    "workers"
  )

  ClusterReceptionistExtension(System).registerService(workers)
}
