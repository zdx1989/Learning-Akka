package com.akkademy

import akka.actor.Actor
import akka.event.Logging

/**
  * Created by zhoudunxiong on 2018/9/1.
  */
class LastStrActor extends Actor {
  var lastStr: String = _
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case s: String =>
      log.info(s"receive string: $s")
      lastStr = s
    case o =>
      log.info(s"receive unknown message: $o")
  }
}
