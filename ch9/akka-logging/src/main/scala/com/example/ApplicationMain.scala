package com.example

import akka.actor.ActorSystem

/**
  * Created by zhoudunxiong on 2018/10/3.
  */
object ApplicationMain extends App {
  val system = ActorSystem("LoggingSystem")
  val loggingActor = system.actorOf(LoggingActor.props, "loggingActor")

  loggingActor ! "message1"
  loggingActor ! "message2"
  loggingActor ! "message3"
  system.shutdown()
}
