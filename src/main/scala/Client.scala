// import scalafx.Includes._
// import scalafx.application.JFXApp
// import scalafx.application.JFXApp.PrimaryStage
// import scalafx.scene.Scene
// import scalafx.scene.text.Text

import Client._
// import App.{system}
import akka.actor.{Actor, ActorRef}
import Server._
//import akka.pattern.ask
import akka.remote.DisassociatedEvent
//import akka.util.Timeout
//import scalafx.application.Platform

import game.Game
import game.GameState

object Client {
	case class StartJoin(server: String, port: String)
	case class CGameState(gameState: GameState)
	case object Begin
}

class Client extends Actor {
	private var gameState: GameState = new GameState()
	// context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])

	override def receive = {
		case Client.StartJoin(server, port) => {
			val serverRef = context.actorSelection(s"akka.tcp://shooter@$server:$port/user/server")
			serverRef ! Server.Join(self)
		}

		case Client.CGameState(gs) => {
			gameState = gs
		}

		case Client.Begin => {
			println("I HAVE BEGUN")
			context.become(begun)			
		}
	}

	def begun: Receive = {
		case _ => println("Begun")
	}
}