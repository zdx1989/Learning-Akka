package com.example

import akka.actor.{Actor, ActorLogging, Props}
import com.example.PongActor.PongMessage

/**
  * Created by zhoudunxiong on 2018/10/2.
  */
class PingActor extends Actor with ActorLogging {
  import PingActor._
  var counter: Int = 0
  val pongActor = context.actorOf(PongActor.props, "pongActor")

  override def receive: Receive = {
    case Initialize =>
      log.info(s"In PingActor - starting ping-pong")
      pongActor ! PingMessage("ping")
    case PongMessage(text) =>
      log.info(s"In PingActor receive message: $text")
      counter += 1
      if (counter == 3) context.system.shutdown()
      else sender() ! PingMessage("ping")
  }
}

object PingActor {
  def props: Props = Props[PingActor]
  case object Initialize
  case class PingMessage(text: String)
}
