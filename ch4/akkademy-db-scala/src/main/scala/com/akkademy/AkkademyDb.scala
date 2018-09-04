package com.akkademy

import akka.actor.{Actor, ActorRef, Status}
import akka.event.Logging
import com.akkademy.AkkademyDb.{Connected, GetRequest, KeyNotFoundException, SetRequest}

import scala.collection.mutable

/**
  * Created by zhoudunxiong on 2018/9/4.
  */
class AkkademyDb extends Actor {
  var map = mutable.HashMap[String, Any]()
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case GetRequest(k, senderRef) =>
      handleGetRequest(k, senderRef)
    case SetRequest(k, v, senderRef) =>
      handleSetRequest(k, v, senderRef)
    case li: List[_] =>
      li.foreach {
        case GetRequest(k, senderRef) => handleGetRequest(k, senderRef)
        case SetRequest(k, v, senderRef) => handleSetRequest(k, v, senderRef)
      }
    case x: Connected =>
      sender() ! x
    case o =>
      log.info(s"receive unknown message: $o")
      sender() ! Status.Failure(new ClassNotFoundException())
  }

  def handleGetRequest(k: String, sendRef: ActorRef): Unit = {
    log.info(s"receive GetRequest - key: $k")
    map.get(k) match {
      case Some(k) => sender() ! k
      case None => sender() ! Status.Failure(KeyNotFoundException(k))
    }
  }

  def handleSetRequest(k: String, v: Any, senderRef: ActorRef): Unit = {
    log.info(s"receive SetRequest - key: $v value: $v")
    map.put(k, v)
    sender() ! Status.Success
  }
}

object AkkademyDb {

  sealed trait Request
  case class GetRequest(key: String, senderRef: ActorRef = ActorRef.noSender) extends Request
  case class SetRequest(key: String, value: Any, senderRef: ActorRef = ActorRef.noSender) extends Request
  case class Connected()
  case class KeyNotFoundException(key: String) extends Exception
}
