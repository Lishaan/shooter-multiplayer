package com.game.scenes

import scalafx.Includes._
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{Label, TextField, Button, Slider}
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.event.ActionEvent

import com.game.App
import com.game.net.{Client, GameService}
import com.game.objects.{Game, Global}

class MainMenu (_width: Double, _height: Double) extends Scene (_width, _height) {

	def this() = this(Global.gameWidth, Global.gameHeight)
	
	if(!App.firstLoad) {
		App.reconfigure()
	}
	App.firstLoad = false
	fill = Scenes.color("Background")

	val centerLayoutY = Global.gameHeight/2
	val layoutYSpacing = 60
	
	val headerText = new Label(s"${Game.name}") {
		prefWidth = 350
		style = "-fx-font: 32 Regular"
		layoutX = Global.gameWidth/2 - (350/2) + 25
		layoutY = 100
	}
	headerText.setTextFill(Color.web("#44f9ff"))

	val clientButton = new Button("Join") {
		prefWidth = 200
		prefHeight = 40
		layoutX = Global.gameWidth/2 - (200/2)
		layoutY = centerLayoutY - layoutYSpacing
		style = Scenes.buttonStyle("Normal")

		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")
		onAction = (e: ActionEvent) => {
			style = Scenes.buttonStyle("onAction")
			App.stage.title = s"${Game.name} - Join Game"
			App.stage.scene = new JoinGame()
		}
	}

	val serverButton = new Button("Host") {
		prefWidth = 200
		prefHeight = 40
		layoutX = Global.gameWidth/2 - ((200)/2)
		layoutY = centerLayoutY
		style = Scenes.buttonStyle("Normal")

		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")
		onAction = (e: ActionEvent) => {
			style = Scenes.buttonStyle("onAction")

			App.stage.title = s"${Game.name} - Game Room"
			App.stage.scene = new GameRoom(true)
			
			App.clientRef ! Client.StartJoin(GameService.HOSTNAME, GameService.PORT)
		}
	}

	val exitButton = new Button("Exit") {
		prefWidth = 200
		prefHeight = 40
		layoutX = Global.gameWidth/2 - (200/2)
		layoutY = centerLayoutY + layoutYSpacing
		style = Scenes.buttonStyle("Normal")

		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")
		onAction = (e: ActionEvent) => {
			App.closeApp()
		}
	}

	val aboutButton = new Button("?") {
		prefWidth = 40
		prefHeight = 40
		layoutX = Global.gameWidth - 60
		layoutY = Global.gameHeight - 60
		style = Scenes.buttonStyle("Normal")

		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")
		onAction = (e: ActionEvent) => {
			style = Scenes.buttonStyle("onAction")
			App.stage.title = s"${Game.name} - About"
			App.stage.scene = new About
		}
	}
	content = List(headerText, clientButton, serverButton, exitButton, aboutButton)
}