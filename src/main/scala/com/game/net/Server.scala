package com.game.net

import scala.collection.mutable.ArrayBuffer

import Client._
import Server._
import akka.actor.{Actor, ActorRef}
//import akka.pattern.ask
import akka.remote.DisassociatedEvent
//import akka.util.Timeout
//import scalafx.collections.ObservableHashSet

//import scala.concurrent.ExecutionContext.Implicits._
//import scala.concurrent.Future
//import scala.concurrent.duration._

import com.game.objects.{GameState, Player}

import com.game.serialization.CustomSerializer

object Server {
    private var currentID: Int = 0

    def getID(): Int = {
        currentID += 1

        return currentID
    }
    // val clients: ArrayBuffer[ActorRef] = ArrayBuffer[ActorRef]()

    case class Join(actor: ActorRef)
    case class UpdateGameState(player: Player)
    case object Start
}

class Server extends Actor {
    val clients: ArrayBuffer[(ActorRef, Int)] = ArrayBuffer[(ActorRef, Int)]()
    val gameState: GameState = new GameState()
    val serializer = new CustomSerializer()
    // context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])

    override def receive: PartialFunction[Any, Unit] = {
        case Server.Join(actor) => {
            val ID: Int = Server.getID()
            clients += ((actor, ID))
            println("JOIN")
            gameState.addPlayer(new Player(ID))
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

        case a: Any => {
            val s = a.toString
            println(s"Default Server ${s}")
        }
    }

    def begun: Receive = {
        case Server.UpdateGameState(player) => {
            println("3")
            gameState.update(player)

            clients.foreach {
                case (clientRef, _) => {
                    clientRef ! Client.CGameState(serializer.toBinary(gameState))
                }
            }
        }

        case _ => println("Begu2n Server")
    }
}