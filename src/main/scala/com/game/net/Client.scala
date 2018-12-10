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
	case class UpdatePlayerID(ID: Int)
}

class Client extends Actor {
	private var serverInfo: ServerInfo = ServerInfo("127.0.0.1", "0")
	private val serverSelection: (ServerInfo) => String = (si: ServerInfo) => s"akka.tcp://shooter@${si.ip}:${si.port}/user/server"
	private val serializer = new CustomSerializer()
	private var playerID: Int = Int.MaxValue
	// context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])

	override def receive: PartialFunction[Any, Unit] = {
		case Client.StartJoin(ip, port) => {
			serverInfo = ServerInfo(ip, port)
			val serverRef = context.actorSelection(serverSelection(serverInfo))
			serverRef ! Server.Join(self)
		}

		case Client.Begin => {
			context.become(begun)			
		}

		case Client.CGameState(bytes) => {
			val value: AnyRef = serializer.fromBinary(bytes, classOf[GameState].getName)
			Game.state = value.asInstanceOf[GameState]
		}

		case Client.PlayerInfo(id) => {
			playerID = id
			Game.playerID = id
		}

		case _ => println("Default Client")
	}

	def begun: Receive = {
		case Client.UpdateGameState(player) => {
			// Game.state.update(player)
			val serverRef = context.actorSelection(serverSelection(serverInfo))
			serverRef ! Server.UpdateGameState(player)
		}

		case Client.CGameState(bytes) => {
			val value: AnyRef = serializer.fromBinary(bytes, classOf[GameState].getName)
			Game.state = value.asInstanceOf[GameState]
		}

		case _ => println("Begun Client")
	}
}