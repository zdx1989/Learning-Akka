package com.akkademy

import akka.actor.Actor
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{MemberEvent, UnreachableMember}
import akka.event.Logging

/**
  * Created by zhoudunxiong on 2018/9/26.
  */
class ClusterController extends Actor {
  val log = Logging(context.system, this)
  val cluster = Cluster(context.system)


  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberEvent], classOf[UnreachableMember])
  }


  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }

  override def receive: Receive = {
    case x: MemberEvent => log.info(s"MemberEvent: $x")
    case x: UnreachableMember => log.info(s"UnreachableMember: $x")
  }
}
