package com.akkademy

import com.akkademy.AkkademyDb.{KeyAlreadyExistsException, KeyNotFoundException}
import org.scalatest.{FunSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by zhoudunxiong on 2018/9/1.
  */
class SClientSpec extends FunSpec with Matchers {
  val client = new SClient("127.0.0.1:2552")

  describe("test SClient") {
    it("should set a value") {
      client.set("zdx", 123)
      val future = client.get("zdx")
      val result = Await.result(future.mapTo[Int], 10.seconds)
      result should be (123)
    }

    it("should set a value if key not exist") {
      client.setIfNotExists("zdx", 123)
      val future = client.get("zdx")
      val result = Await.result(future.mapTo[Int], 10.seconds)
      result should be (123)
    }

    it("should not set value if key already exist") {
      client.set("zdx", 123)
      val future = client.setIfNotExists("zdx", "ygy")
      intercept[KeyAlreadyExistsException] {
        Await.result(future.mapTo[String], 10.seconds)
      }
    }

    it("should delete a value by key") {
      client.set("zdx", 123)
      client.delete("zdx")
      val future = client.get("zdx")
      intercept[KeyNotFoundException] {
        Await.result(future.mapTo[Int], 10.seconds)
      }
    }
  }
}
