package com.example

import akka.actor.{Actor, ActorLogging, Props}

/**
  * Created by zhoudunxiong on 2018/10/3.
  */
class LoggingActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case x =>
      log.info(s"LoggingActor receive event: ${x.toString}")
  }
}

object LoggingActor {
  def props: Props = Props[LoggingActor]

  case class PingMessage(text: String)
}
