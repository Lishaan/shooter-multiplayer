package com.game

import akka.actor.{ActorSystem, Props, Terminated, ActorRef}
import scalafx.application.Platform
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.collections.{ObservableBuffer, ObservableHashSet}
import scalafx.scene.Scene

import scalafx.beans.property.PropertyIncludes._
import scalafx.beans.property.BooleanProperty

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import com.game.net.{Client, Server}
import com.game.objects.{Game, GameState}
import com.game.scenes.{MainMenu, GameRoom, ClientSetup}

object App extends JFXApp {
    var nameList = new ObservableBuffer[String]()
    var firstLoad = true
    var system = ActorSystem("shooter", Server.getConfig())
    var serverRef = system.actorOf(Props[Server], "server")
    var clientRef = system.actorOf(Props[Client], "client")

    stage = new PrimaryStage {
		title = s"${Game.name} - Main Menu"
		scene = new MainMenu()
		resizable = false
	}

    def runGame(): Unit = {
        stage = new Game(App.system, App.serverRef, App.clientRef)
    }

    def stopProcesses(): Unit = {
        try {
		    Await.result(system.terminate, 2 second)
	    } catch {
	    	case e: Exception => println("Caught Exception")
            case _: Throwable => println("Random Exception At stopping processes")
	    }
        
    }

    def reconfigure(): Unit = {
        stopProcesses()
        Server.clients = new ObservableHashSet[(ActorRef, Int)]()
        Server.resetID

        Game.playerID = Int.MaxValue
        Game.state = new GameState()
        Game.ended = false

        App.system = ActorSystem("shooter", Server.getConfig())
        App.serverRef = system.actorOf(Props[Server], "server")
        App.clientRef = system.actorOf(Props[Client], "client")
        App.nameList = new ObservableBuffer[String]()

        GameRoom.nameList.items = new ObservableBuffer[String]()
        GameRoom.startGameButtonDisabled = new BooleanProperty(GameRoom, "startGameButtonDisabled", true)
    }

    def showServerFullDialog(message: String): Unit ={
        Platform.runLater {
            new Alert(AlertType.Error) {
                initOwner(stage)
                title = "Server Full"
                headerText = "Max Amount of Player Reached"
                contentText = message
            }.showAndWait()

            App.stage.title = s"${Game.name} - Client Setup"
			App.stage.scene = new ClientSetup()
        }
    }

    def showDisconnectedDialog(message: String): Unit ={
        Platform.runLater {
            new Alert(AlertType.Error) {
                initOwner(stage)
                title = "Disconnected"
                headerText = "Server has shut down"
                contentText = message
            }.showAndWait()
            
            App.stage.title = s"${Game.name} - Main Menu"
			App.stage.scene = new MainMenu
        }
    }
    def closeApp(): Unit = {
        stopProcesses()
        stage.close
    }

    def getNameList(): ObservableBuffer[String] = ObservableBuffer[String]((for (name <- App.nameList) yield name).toList)
}