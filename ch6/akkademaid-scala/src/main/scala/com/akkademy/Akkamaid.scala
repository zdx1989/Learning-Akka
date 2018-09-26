package com.akkademy

import akka.actor.{ActorSystem, Props}

/**
  * Created by zhoudunxiong on 2018/9/26.
  */
object Akkamaid extends App {
  val system = ActorSystem("Akkademy")
  val clusterController = system.actorOf(Props[ClusterController], "clusterController")
}
