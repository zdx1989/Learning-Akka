package com.example

import akka.actor.ActorSystem
import akka.pattern.CircuitBreaker
import akka.util.Timeout
import akka.pattern.ask
import com.example.PingActor.{Initialize, PingMessage}

import scala.concurrent.duration._

/**
  * Created by zhoudunxiong on 2018/10/2.
  */
object ApplicationMain extends App {
  val system = ActorSystem("MyActorSystem")
  implicit val es = system.dispatcher
  implicit val timeout = Timeout(2.seconds)

  val pingActorWithMailBox = system.actorOf(
    PingActor.props.withMailbox("akka.actor.boundedmailbox"),
    "pingActor"
  )
  pingActorWithMailBox ! Initialize
  val pongActor = system.actorOf(PongActor.props, "pongActor2")
  val breaker =
    new CircuitBreaker(
      system.scheduler,
      maxFailures =  1,
      callTimeout = 1.seconds,
      resetTimeout = 1.seconds
    )
    .onOpen(println("circuit breaker opened!"))
    .onClose(println("circuit breaker closed!"))
    .onHalfOpen(println("circuit breaker half-open"))

  val future1 = breaker.withCircuitBreaker(
    pongActor ? PingMessage("ping")
  )

  val future2 = breaker.withCircuitBreaker(
    pongActor ? PingMessage("ping")
  )

  future1.foreach(x => println(s"response1: $x"))
  future2.foreach(x => println(s"response2: $x"))
  Thread.sleep(1000)
  system.awaitTermination()

}
