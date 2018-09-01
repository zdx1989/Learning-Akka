package com.akkademy

import akka.actor.Status.Success
import akka.actor.{Actor, Status}
import akka.event.Logging
import com.akkademy.AkkademyDb._

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
      sender() ! Status.Success(k)
    case GetRequest(k) =>
      log.info(s"receive GetRequest - key: $k")
      map.get(k) match {
        case Some(v) => sender() ! v
        case None => sender() ! Status.Failure(KeyNotFoundException(k))
      }
    case SetIfNotExists(k, v) =>
      log.info(s"receive SetIfNotExists - key: $k value: $v")
      map.get(k) match {
        case None =>
          map.put(k, v)
          sender() ! Status.Success(k)
        case _ => sender() ! Status.Failure(KeyAlreadyExistsException(k))
      }
    case Delete(k) =>
      log.info(s"receive Delete - key: $k")
      map.remove(k)
      sender() ! Status.Success(k)
    case o =>
      log.info(s"receive unknown message")
      sender() ! Status.Failure(new ClassNotFoundException)
  }
}

object AkkademyDb {

  sealed trait Request
  case class GetRequest(key: String) extends Request
  case class SetRequest(key: String, value: Any) extends Request
  case class SetIfNotExists(key: String, value: Any) extends Request
  case class Delete(key: String) extends Request

  case class KeyNotFoundException(key: String) extends Exception
  case class KeyAlreadyExistsException(key: String) extends Exception
}
