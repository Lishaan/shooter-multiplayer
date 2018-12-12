package com.game.scenes

import scalafx.Includes._

import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{Label, TextField, Button}
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.event.ActionEvent

import com.game.objects.{Game, Global}
import com.game.net.Client
import com.game.App

class ClientSetup (_width: Double, _height: Double) extends Scene (_width, _height) {

	def this() = this(Global.gameWidth, Global.gameHeight)

	fill = Scenes.color("Background")

	val back = new Button("«") {
		prefWidth = 40
		layoutX = 20
		layoutY = 20
		style = Scenes.buttonStyle("Normal")
		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")

		onAction = (e: ActionEvent) => {
			App.stage.title = s"${Game.name} - Main Menu"
			App.stage.scene = new MainMenu
		}
	}

	val pushY = 100

	val headerText = new Label("Client Setup") {
		prefWidth = 250
		style = "-fx-font: 35 Regular"
		layoutX = Global.gameWidth/2 - (250/2) + 15 + 4
		layoutY = 120
	}
	headerText.setTextFill(Color.web("#44f9ff"))

	val ip_label = new Label("IP Address") {
		prefWidth = 200
		style = "-fx-font: 24 Regular"
		layoutX = Global.gameWidth/2 - (200/2) - 50 + 30
		layoutY = 140+pushY+0 - 10
	}
	ip_label.setTextFill(Color.web("#00BCC5"))

	val ip_textField = new TextField {
		prefWidth = 100
		layoutX = Global.gameWidth/2 - (100/2) + 15 + 20 + 30
		layoutY = 140+pushY+1 - 10
	}

	val port_label = new Label("Port") {
		prefWidth = 200
		style = "-fx-font: 24 Regular"
		layoutX = Global.gameWidth/2 - (200/2) - 50 + 30
		layoutY = 200+pushY+0 - 10
	}
	port_label.setTextFill(Color.web("#00BCC5"))

	val port_textField = new TextField {
		prefWidth = 100
		layoutX = Global.gameWidth/2 - (100/2) + 15 + 20 + 30
		layoutY = 200+pushY+1 - 10
	}

	val connectButton = new Button("Connect") {
		prefWidth = 150
		layoutX = Global.gameWidth/2 - (150/2)
		layoutY = 320+pushY+6
		style = Scenes.buttonStyle("Normal")
		onMouseEntered = (e: MouseEvent) => style = Scenes.buttonStyle("onEntered")
		onMouseExited = (e: MouseEvent) => style = Scenes.buttonStyle("onExited")

		onAction = (e: ActionEvent) => { 
			style = Scenes.buttonStyle("onAction")
			val ip = ip_textField.getText
			val port = port_textField.getText

            App.clientRef ! Client.StartJoin(ip, port)
			App.stage.scene = new GameRoom()
		}
	}


	content = List(
        back, headerText, 
        ip_label, ip_textField, 
        port_label, port_textField, 
        connectButton
    )
}