package com.game.net

import scalafx.collections.{ObservableHashSet, ObservableBuffer}

import com.typesafe.config.{Config, ConfigFactory}

import akka.actor.{Actor, ActorRef}
import akka.remote.DisassociatedEvent

import com.game.App
import com.game.objects.{Game, GameState, Player}
import com.game.serialization.CustomSerializer

import Client._
import Server._

object Server {
    private var currentID: Int = 0
    var clients = new ObservableHashSet[(ActorRef, Int)]()
    
    def resetID:Unit = {
        currentID = 0
    }

    def getID(): Int = {
        currentID += 1
        
        if (currentID > 2) (currentID = 2)
        
        return currentID
    }

    case class Join(actor: ActorRef)
    case class UpdateGameState(player: Player)
    case object Start
    case object ServerLeftRoom
    case class ClientLeftRoom(actorRef:ActorRef,id: Int)

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
    @volatile var gameState: GameState = new GameState()
    val serializer = new CustomSerializer()
   
    context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])

    override def receive: PartialFunction[Any, Unit] = {
        case Server.Join(actor) => {
            val ID: Int = Server.getID()
            if (Server.clients.size >= 2) {
                sender ! Client.ServerFull
            } else {
                Server.clients += ((actor, ID))
                App.nameList += actor.toString
                actor ! Client.UpdateNameList(App.nameList.toList)
                sender ! Client.PlayerInfo(ID)
            }
        }

        case Server.Start => {
            var count = 0
            Server.clients.foreach {
                case (clientRef, playerID) => {
                    count += 1
                    clientRef ! Client.Begin
                }
            }
            println("UEGKJGWKVGHE: " + count)
            context.become(begun)
        }

        case Server.ServerLeftRoom => {
            if (Server.clients.toList.length-1 > 0) {
                Server.clients.toList(1)._1 ! Client.ServerLeftRoom
            }
        }

        case Server.ClientLeftRoom(remoteClientRef,clientID) => {
                val t = (remoteClientRef,clientID)
                Server.clients -= t 
                App.nameList.remove(1)
        }
     
        case _ => 
    }

    def begun: Receive = {
        case Server.UpdateGameState(player) => {
            gameState.update(player)
            gameState.updateIntersections()

            Server.clients.foreach {
                case (clientRef, _) => {
                    clientRef ! Client.CGameState(serializer.toBinary(gameState))
                }
            }

            // gameState.print()
        }

        case _ =>
    }
}