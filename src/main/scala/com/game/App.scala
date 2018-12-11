package com.game

import akka.actor.{ActorSystem, Props}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage

import com.game.net.{Client, Server}
import com.game.objects.Game
import com.game.scenes.MainMenu

object App extends JFXApp {
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

    def reconfigure(): Unit = {
        App.system = ActorSystem("shooter", Server.getConfig())
        App.serverRef = system.actorOf(Props[Server], "server")
        App.clientRef = system.actorOf(Props[Client], "client")
    }

    def closeApp(): Unit = {
        system.stop(serverRef)
        system.stop(clientRef)
        system.terminate
        stage.close
    }
}