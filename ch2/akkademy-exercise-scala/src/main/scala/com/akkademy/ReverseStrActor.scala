package com.akkademy

import akka.actor.{Actor, Status}
import akka.event.Logging

/**
  * Created by zhoudunxiong on 2018/9/1.
  */
class ReverseStrActor extends Actor {
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case s: String =>
      log.info(s"receive string: $s")
      sender() ! s.reverse
    case o =>
      log.info(s"receive unknown message")
      sender() ! Status.Failure(new ClassNotFoundException)
  }
}
