package com.game.net

import scala.collection.mutable.ArrayBuffer

import com.typesafe.config.{Config, ConfigFactory}
import akka.actor.{Actor, ActorRef}
import akka.remote.DisassociatedEvent

import com.game.objects.{Game, GameState, Player}
import com.game.serialization.CustomSerializer

import Client._
import Server._

object Server {
    private var currentID: Int = 0

    def getID(): Int = {
        currentID += 1
        return currentID
    }

    case class Join(actor: ActorRef)
    case class UpdateGameState(player: Player)
    case object Start

    def getConfig(): Config = {
        println("Binding to: " + GameService.HOSTNAME)
        val config = ConfigFactory.parseString(s"""
        |  akka {
        |    loglevel = "INFO"
        |  
        |    actor {
        |      provider = "akka.remote.RemoteActorRefProvider"
        |      serializers {
        |          java = "akka.serialization.JavaSerializer"
        |          proto = "akka.remote.serialization.ProtobufSerializer"
        |          custom = "com.game.serialization.CustomSerializer"
        |      }
        |  
        |      serialization-bindings {
        |          "com.game.objects.GameState" = custom
        |      }
        |    }
        |  
        |    remote {
        |      enabled-transports = ["akka.remote.netty.tcp"]
        |      netty.tcp {
        |        hostname = "${GameService.HOSTNAME}"
        |        port = ${GameService.PORT}            
        |      }
        |  
        |      log-sent-messages = on
        |      log-received-messages = on
        |    }
        |  
        |  }
        |  
        """.stripMargin)

        return config.withFallback(ConfigFactory.load())
    }
}

class Server extends Actor {
    val clients: ArrayBuffer[(ActorRef, Int)] = ArrayBuffer[(ActorRef, Int)]()//send to app for display in gameroom
    val serializer = new CustomSerializer()
    @volatile var gameState: GameState = new GameState()
    // context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])

    override def receive: PartialFunction[Any, Unit] = {
        case Server.Join(actor) => {
            println("Actor Joined")
            val ID: Int = Server.getID()
            clients += ((actor, ID))
        }

        case Server.Start => {
            clients.foreach {
                case (clientRef, playerID) => {
                    clientRef ! Client.CGameState(serializer.toBinary(gameState))
                    clientRef ! Client.PlayerInfo(playerID)
                }
            }

            clients.foreach {
                case (clientRef, playerID) => {
                    clientRef ! Client.Begin
                }
            }

            context.become(begun)
            gameState.print()
        }
    }

    def begun: Receive = {
        case Server.UpdateGameState(player) => {
            gameState.update(player)
            gameState.updateIntersections()

            clients.foreach {
                case (clientRef, _) => {
                    clientRef ! Client.CGameState(serializer.toBinary(gameState))
                }
            }

            gameState.print()
        }

        case _ => println("Begun Server")
    }
}