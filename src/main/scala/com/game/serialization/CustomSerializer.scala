package com.game.serialization

import akka.actor.{ActorRef, ActorSystem}
import akka.serialization._
import com.typesafe.config.ConfigFactory

import java.io.{ByteArrayOutputStream, ObjectOutputStream, ObjectInputStream, ByteArrayInputStream}

import com.game.objects.GameState

class CustomSerializer extends SerializerWithStringManifest {
    def identifier = 78987832

    def manifest(obj: AnyRef): String = obj.getClass.getName

    private val ProcessingGameState = classOf[GameState].getName

    def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = {
        manifest match {
            case ProcessingGameState => GameState.parseFrom(bytes)
        }
    }

    def toBinary(obj: AnyRef): Array[Byte] = {
        val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
        val oos = new ObjectOutputStream(stream)
        oos.writeObject(obj)
        oos.close()
        return stream.toByteArray
    }
}