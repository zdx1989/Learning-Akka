package com.akkachat

/**
  * Created by zhoudunxiong on 2018/10/2.
  */
case class JoinChatRoom(userRef: UserRef)

case class PostToChatRoom(line: String, username: String)