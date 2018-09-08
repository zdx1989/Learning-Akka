package com.akkademy

import akka.actor.FSM
import com.akkademy.AkkademyDb.Request
import com.akkademy.FSMClientActor._

/**
  * Created by zhoudunxiong on 2018/9/8.
  */
class FSMClientActor(remoteAddress: String) extends FSM[State, RequestQueue]{
  val remoteActor = context.system.actorSelection(remoteAddress)

  startWith(DisConnected, List.empty[Request])

  when(DisConnected) {
    case Event(_: AkkademyDb.Connected, container: RequestQueue) =>
      if (container.headOption.isEmpty)
        goto(Connected)
      else
        goto(ConnectedAndPending)
    case Event(r: Request, container: RequestQueue) =>
      remoteActor ! new AkkademyDb.Connected()
      stay().using(container :+ r)
    case o =>
      stay()

  }

  when(Connected) {
    case Event(r: Request, container: RequestQueue) =>
      goto(ConnectedAndPending).using(container :+ r)
  }

  when(ConnectedAndPending) {
    case Event(Flush, container: RequestQueue) =>
      remoteActor ! container
      goto(Connected).using(Nil)
    case Event(r: Request, container: RequestQueue) =>
      stay().using(container :+ r)
  }

  initialize()

}

object FSMClientActor {
  sealed trait State
  case object DisConnected extends State
  case object Connected extends State
  case object ConnectedAndPending extends State

  case object Flush
  case object ConnectedMsg

  type RequestQueue = List[Request]
}
