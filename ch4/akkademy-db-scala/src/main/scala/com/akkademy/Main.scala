package com.akkademy

import akka.actor.{ActorSystem, Props}

/**
  * Created by zhoudunxiong on 2018/9/4.
  */
object Main extends App {
  val system = ActorSystem("akkademy")
  val akkademyDb = system.actorOf(Props[AkkademyDb], "akkademy-db")
}
