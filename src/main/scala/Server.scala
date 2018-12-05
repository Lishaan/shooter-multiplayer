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

object Server {
    // val clients: ArrayBuffer[ActorRef] = ArrayBuffer[ActorRef]()
    val gameState: GameState = new GameState()

    case class Join(actor: ActorRef)
    case object Start
}

class Server extends Actor {
    // context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])

    override def receive: PartialFunction[Any, Unit] = {
        case Server.Join(actor) => {
            gameState.players += new Player(actor)
            // players += acto
        }

        case Server.Start => {
            gameState.players.foreach(player => {
                player.ref ! Client.CGameState(gameState)
                println(player.ref)
            })

            gameState.players.foreach(player => {
                player.ref ! Client.Begin
                println(player.ref)
            })
        }
    }
}