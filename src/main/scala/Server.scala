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

import game.GameState
import game.Player

import serialization.CustomSerializer

object Server {
    private var currentID: Int = 0

    def getID(): Int = {
        currentID += 1

        return currentID
    }
    // val clients: ArrayBuffer[ActorRef] = ArrayBuffer[ActorRef]()

    case class Join(actor: ActorRef)
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

            gameState.addPlayer(new Player(ID))
        }

        case Server.Start => {
            clients.foreach {
                case (clientRef, playerID) => {
                    clientRef ! serializer.toBinary(gameState)
                    clientRef ! playerID
                }
            }

            clients.foreach {
                case (clientRef, playerID) => {
                    clientRef ! Client.Begin
                }
            }
        }
    }
}