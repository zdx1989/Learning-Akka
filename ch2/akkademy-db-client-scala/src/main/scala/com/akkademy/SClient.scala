package com.akkademy

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.akkademy.AkkademyDb.{Delete, GetRequest, SetIfNotExists, SetRequest}

import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * Created by zhoudunxiong on 2018/9/1.
  */
class SClient(remoteAddress: String) {
  private val system = ActorSystem("LocalSystem")
  private implicit val timeout = Timeout(2.seconds)
  private val remoteDb = system.actorSelection(
    s"akka.tcp://akkademy@$remoteAddress/user/akkademy-db"
  )

  def get(key: String): Future[Any] = {
    remoteDb ? GetRequest(key)
  }

  def set(key: String, value: Any): Future[Any] = {
    remoteDb ? SetRequest(key, value)
  }

  def setIfNotExists(key: String, value: Any): Future[Any] = {
    remoteDb ? SetIfNotExists(key, value)
  }

  def delete(key: String): Future[Any] = {
    remoteDb ? Delete(key)
  }
}
