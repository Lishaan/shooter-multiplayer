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
    val clients: ArrayBuffer[ActorRef] = ArrayBuffer[ActorRef]()

    case class Join(actor: ActorRef)
    case object Start
}

class Server extends Actor {
    val gameState: GameState = new GameState()
    val serializer = new CustomSerializer()
    // context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])

    override def receive: PartialFunction[Any, Unit] = {
        case Server.Join(actor) => {
            // gameState.players += new Player(actor)
            Server.clients += actor
        }

        case Server.Start => {
            Server.clients.foreach(client => {
                client ! serializer.toBinary(gameState)
            })

            Server.clients.foreach(client => {
                client ! Client.Begin
            })
        }
    }
}