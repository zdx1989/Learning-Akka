package com.akkachat

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestProbe}
import akka.pattern.ask
import akka.util.Timeout
import org.scalatest.{FunSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._


/**
  * Created by zhoudunxiong on 2018/10/2.
  */
class ChatRoomSpec extends FunSpec with Matchers {
  implicit val system = ActorSystem()

  describe("Given a Chatroom has no user") {
    val chatRoomRef = TestActorRef[ChatRoom](ChatRoom.props, "chatRoom")
    val chatRoom = chatRoomRef.underlyingActor
    val joinedUsers = chatRoom.joinedUsers
    it("should create a empty chatroom") {
      joinedUsers.size should be (0)
    }

    describe("When receive a request from user to join chatroom") {
      val userRef = UserRef(system.deadLetters, "testUser")
      chatRoomRef ! JoinChatRoom(userRef)
      it("should add the user to the list of joined users") {
        chatRoom.joinedUsers.head should be (userRef)
      }
    }
  }

  describe("Given a chatroom has a hostory") {
    implicit val timeout = Timeout(2.seconds)
    val chatroomRef = TestActorRef[ChatRoom](ChatRoom.props, "chatroom")
    val chatRoom = chatroomRef.underlyingActor
    chatRoom.chatHistory = chatRoom.chatHistory :+ PostToChatRoom("Hello I'm testUser", "testUser")
    describe("When user join the chatroom") {
      val userRef = UserRef(system.deadLetters, "testA")
      val future = chatroomRef ? JoinChatRoom(userRef)
      val result = Await.result(future.mapTo[Seq[PostToChatRoom]], 2.seconds)
      it("user should receive a history") {
        result should be (Seq(PostToChatRoom("Hello I'm testUser", "testUser")))
      }
    }
  }

  describe("Given a chatroom has joined user") {
    val chatroomRef = TestActorRef[ChatRoom](ChatRoom.props, "chatroom2")
    val chatroom = chatroomRef.underlyingActor
    val testProbe = TestProbe()
    val userRef = UserRef(testProbe.ref, "testUser")
    chatroom.joinedUsers = chatroom.joinedUsers :+ userRef
    describe("when someone post to chatroom") {
      val msg = PostToChatRoom("Hello, I'm testA", "testA")
      chatroomRef.tell(msg, testProbe.ref)
      it("joined user should receive a message") {
        testProbe.expectMsg(msg)
      }
    }
  }
}
