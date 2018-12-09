package com.game.net

import Client._
// import App.{system}
import akka.actor.{Actor, ActorRef, ActorSelection}
import Server._
//import akka.pattern.ask
import akka.remote.DisassociatedEvent
//import akka.util.Timeout
//import scalafx.application.Platform

import com.game.objects.{Game, GameState, Player}

import com.game.serialization.CustomSerializer

object Client {
	case class ServerInfo(ip: String, port: String)
	case class StartJoin(server: String, port: String)
	case class CGameState(bytes: Array[Byte])
	case class PlayerInfo(id: Int)
	case object Begin
	case class UpdateGameState(player: Player)
}

class Client extends Actor {
	private var serverInfo: ServerInfo = ServerInfo("127.0.0.1", "0")
	private val serverSelection: (ServerInfo) => String = (si: ServerInfo) => s"akka.tcp://shooter@${si.ip}:${si.port}/user/server"
	private val serializer = new CustomSerializer()
	private var playerID: Int = Int.MaxValue
	private var gameState: GameState = new GameState()
	// context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])

	override def receive: PartialFunction[Any, Unit] = {
		case Client.StartJoin(ip, port) => {
			serverInfo = ServerInfo(ip, port)
			val serverRef = context.actorSelection(serverSelection(serverInfo))
			serverRef ! Server.Join(self)
		}

		case Client.Begin => {
			println("I HAVE BEGUN")
			context.become(begun)			
		}

		case Client.CGameState(bytes) => {
			val value: AnyRef = serializer.fromBinary(bytes, classOf[GameState].getName)
			gameState = value.asInstanceOf[GameState]

			gameState.print()
		}

		case Client.PlayerInfo(id) => {
			playerID = id
		}

		case Client.UpdateGameState => {
			println("0.5")
			val serverRef = context.actorSelection(serverSelection(serverInfo))
			serverRef ! Server.UpdateGameState(gameState.getPlayerByID(playerID))
		}

		case _ => println("Default Client")
	}

	def begun: Receive = {
		case Client.UpdateGameState(player) => {
			println("1")
			gameState.update(player)
			val serverRef = context.actorSelection(serverSelection(serverInfo))
			serverRef ! Server.UpdateGameState(gameState.getPlayerByID(playerID))
		}

		case Client.CGameState(bytes) => {
			println("2")
			val value: AnyRef = serializer.fromBinary(bytes, classOf[GameState].getName)
			gameState = value.asInstanceOf[GameState]
			Game.state = gameState

			gameState.print()
		}

		case _ => println("Begun1 Client")
	}
}