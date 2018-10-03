package com.example

import akka.actor.{Actor, ActorLogging, Props}
import com.example.LoggingActor.PingMessage
import com.example.PongActor.PongMessage

/**
  * Created by zhoudunxiong on 2018/10/3.
  */
class PongActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case PingMessage(text) =>
      log.info(s"In PongActor receive message: $text")
      sender() ! PongMessage("pong")
  }
}

object PongActor {

  def props: Props = Props[PongActor]

  case class PongMessage(text: String)
}
