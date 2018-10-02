package com.akkachat

import akka.actor.{Actor, ActorLogging, Props}

/**
  * Created by zhoudunxiong on 2018/10/2.
  */
class ChatRoom extends Actor with ActorLogging {
  var joinedUsers: Seq[UserRef] = Seq.empty
  var chatHistory: Seq[PostToChatRoom] = Seq.empty

  override def receive: Receive = {
    case JoinChatRoom(userRef) =>
      sender() ! joinChatRoom(userRef)
    case x: PostToChatRoom =>
      postToChatRoom(x)
    case _ =>
      log.info(s"receive unknown message")
  }

  def joinChatRoom(userRef: UserRef): Seq[PostToChatRoom] = {
    joinedUsers = joinedUsers :+ userRef
    chatHistory.take(10)
  }

  def postToChatRoom(p: PostToChatRoom): Unit = {
    chatHistory = chatHistory :+ p
    joinedUsers.foreach { case UserRef(ref, _) =>
      ref ! p
    }
  }
}

object ChatRoom {
  def props: Props = Props[ChatRoom]
}
