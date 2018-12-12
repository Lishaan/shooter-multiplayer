package com.game.scenes

import scalafx.Includes._
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{Label, TextField, Button, ListView}
import scalafx.event.ActionEvent
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.application.Platform
import scalafx.beans.property.PropertyIncludes._
import scalafx.beans.property.BooleanProperty

import com.game.objects.{Game, Global}
import com.game.net.{Client, Server, GameService}
import com.game.net.Server._
import com.game.net.Client._

import akka.actor.{ActorRef, ActorSystem, Props}
import com.game.App

object GameRoom {
	var startGameButtonDisabled: BooleanProperty = new BooleanProperty(GameRoom, "startGameButtonDisabled", true)

	var nameList: ListView[String] = new ListView[String](List()) {
        layoutX = Global.gameWidth/2 - (400/2)
		layoutY = 200
        maxHeight = 100
		minWidth = 400
        opacity = 1
    }
}

class GameRoom (private val isServer: Boolean = false) extends Scene (Global.gameWidth, Global.gameHeight) {
	fill = Scenes.color("Background")

	val back = new Button("«") {
		prefWidth = 40
		layoutX = 20
		layoutY = 20
		style = Scenes.buttonStyle("Normal")
		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")

		onAction = (e: ActionEvent) => {
   
			App.nameList.clear() 
            if (isServer) {
				App.serverRef ! Server.ServerLeftRoom
				App.stage.title = s"${Game.name} - Main Menu"
				App.stage.scene = new MainMenu
            } else {
				App.clientRef ! Client.ClientLeftRoom
				App.stage.title = s"${Game.name} - Client Setup"
				App.stage.scene = new ClientSetup()

            }

		}
	}

	val pushY = 100

	val headerText = new Label("Game Room") {
		prefWidth = 250
		style = "-fx-font: 35 Regular"
		layoutX = Global.gameWidth/2 - (250/2) + 15 + 4
		layoutY = 120
	}
	headerText.setTextFill(Color.web("#44f9ff"))

    val currentIP: String = GameService.HOSTNAME
    val currentPORT: String = GameService.PORT

    val hostingStr: String = s"Server hosted at $currentIP:$currentPORT"
	val hosting_label = new Label(hostingStr) {
		prefWidth = 300
		style = "-fx-font: 12 Regular"
		layoutX = Global.gameWidth - 300
		layoutY = 36
	}
	hosting_label.setTextFill(Color.web("#44f9ff"))

	val startGameButton = new Button("Start Game") {
		prefWidth = 150
		layoutX = Global.gameWidth/2 - (150/2)
		layoutY = 320+pushY+6
		style = Scenes.buttonStyle("Smaller")
		disable = true

		onAction = (e: ActionEvent) => {
            if (isServer) {
                App.serverRef ! Server.Start
            }
            
            App.runGame()
        }
	}

	GameRoom.startGameButtonDisabled.onChange((observable, oldValue, newValue) => {
		if (!isServer) {
			this.startGameButton.disable = false
			Platform.runLater {
				App.runGame()
			}
		}
	})

    App.nameList.onChange((a, b) => {		
		Platform.runLater(

			GameRoom.nameList.items = App.getNameList()
		)
		if ((isServer) && (App.nameList.length == 2)) {
			startGameButton.disable = false
		}

	})
	content = List(
        back, headerText, 
        GameRoom.nameList
    )

    if (isServer) {
        content += hosting_label
        content += startGameButton
    }
}