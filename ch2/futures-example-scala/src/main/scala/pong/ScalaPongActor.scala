package pong

import akka.actor.{Actor, Status}

/**
  * Created by zhoudunxiong on 2018/9/1.
  */
class ScalaPongActor extends Actor {

  override def receive: Receive = {
    case "Ping" => sender() ! "Pong"
    case _ => sender() ! Status.Failure(new Exception("unknown message"))
  }
}
