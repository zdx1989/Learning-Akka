package com.akkademy

import akka.actor.{Actor, Stash}
import com.akkademy.AkkademyDb.{Connected, Request}

/**
  * Created by zhoudunxiong on 2018/9/6.
  */
class HotswapClientActor(remoteAddress: String) extends Actor with Stash {
  val remoteActor = context.system.actorSelection(remoteAddress)

  override def receive: Receive = {
    case _ : Connected =>
      unstashAll()
      context.become(online)
    case x: Request =>
      remoteActor ! new Connected
      stash()
  }

  def online: Receive = {
    case x: Request =>
      remoteActor forward x
    case _: DisConnected =>
      context.unbecome()
  }
}

class DisConnected
