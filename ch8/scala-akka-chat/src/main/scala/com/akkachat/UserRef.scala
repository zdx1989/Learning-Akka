package com.akkachat

import akka.actor.ActorRef

/**
  * Created by zhoudunxiong on 2018/10/2.
  */
case class UserRef(ref: ActorRef, username: String)
