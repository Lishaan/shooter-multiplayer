package com.game.net

import scalafx.collections.{ObservableHashSet, ObservableBuffer}
import scalafx.application.Platform

import akka.actor.{Actor, ActorRef, ActorSelection}
import akka.remote.DisassociatedEvent

import com.game.App
import com.game.objects.{Game, GameState, Player}
import com.game.scenes.GameRoom
import com.game.serialization.CustomSerializer

import Client._
import Server._

object Client {
	case class ServerInfo(ip: String, port: String)
	case class StartJoin(server: String, port: String)
	case class CGameState(bytes: Array[Byte])
	case class PlayerInfo(id: Int)
	case object Begin
	case class UpdateGameState(player: Player)
	case class UpdatePlayerID(ID: Int)
	case class UpdateNameList(nameList: List[String])
	case object ClientLeftRoom
	case object ServerLeftRoom
	case object ServerFull
}

class Client extends Actor {
	private var serverInfo: ServerInfo = ServerInfo("127.0.0.1", "0")
	private val serverSelection: (ServerInfo) => String = (si: ServerInfo) => s"akka.tcp://shooter@${si.ip}:${si.port}/user/server"
	private val serializer = new CustomSerializer()
	private var playerID: Int = Int.MaxValue
	
	context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])

	override def receive: PartialFunction[Any, Unit] = {
		case Client.StartJoin(ip, port) => {
			context.become(joined)
			serverInfo = ServerInfo(ip, port)
			val serverRef = context.actorSelection(serverSelection(serverInfo))
			serverRef ! Server.Join(self)
		}
	}


	def joined: Receive = {
		case Client.PlayerInfo(id) => {
			playerID = id
			Game.playerID = id
		}

		case Client.Begin => {
			Game.state = new GameState()
			context.become(begun)	
			GameRoom.startGameButtonDisabled.update(!GameRoom.startGameButtonDisabled.value)
		}

		
		case Client.UpdateNameList(serverNameList) => {
			App.nameList.clear()
			serverNameList.foreach(elem => App.nameList += elem)
		}

		case Client.ClientLeftRoom => {
			val serverRef = context.actorSelection(serverSelection(serverInfo))
			serverRef ! Server.ClientLeftRoom(self,Game.playerID)
			context.unbecome()			
		}

		case Client.ServerLeftRoom => {
			App.nameList.clear()
		}
		
		case Client.ServerFull => {
			context.unbecome()
			App.showServerFullDialog("Please connect to another server")
		}

		case DisassociatedEvent(local, remote, inbound) =>{
			if (!inbound) {
  	 	  		App.showDisconnectedDialog(s"$remote has been disconnected, re-directing to main menu")
			}
		}

		case _ =>
	}
	def begun: Receive = {
		case Client.UpdateGameState(player) => {
			Game.state.update(player)
			val serverRef = context.actorSelection(serverSelection(serverInfo))
			serverRef ! Server.UpdateGameState(player)
		}

		case Client.CGameState(bytes) => {
			val value: AnyRef = serializer.fromBinary(bytes, classOf[GameState].getName)
			Game.state = value.asInstanceOf[GameState]
		}

		case _ =>
	}
}