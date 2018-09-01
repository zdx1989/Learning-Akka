package com.akkademy

import akka.actor.Actor
import akka.event.Logging
import com.akkademy.AkkademyDb.SetRequest

import scala.collection.mutable

/**
  * Created by zhoudunxiong on 2018/9/1.
  */
class AkkademyDb extends Actor {
  var map = mutable.HashMap[String, Any]()
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case SetRequest(k, v) =>
      log.info(s"receive SetRequest - key: $k value: $v")
      map.put(k, v)
    case o =>
      log.info(s"receive unknown message: $o")
  }
}

object AkkademyDb {

  sealed trait Request
  case class SetRequest(key: String, value: Any) extends Request
}
