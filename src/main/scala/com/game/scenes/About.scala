package com.game.scenes

import scalafx.Includes._
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{Label, TextField, Button, Slider}
import scalafx.event.ActionEvent
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color

import com.game.objects.{Game, Global}
import com.game.App

class About (_width: Double, _height: Double) extends Scene (_width, _height) {

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
	val pullY = -40

	val headerText = new Label("About") {
		prefWidth = 250
		style = "-fx-font: 35 Regular"
		layoutX = Global.gameWidth/2 - (250/2) + 75
		layoutY = 120+pullY
	}
	headerText.setTextFill(Color.web("#44f9ff"))

	val aboutGameStr = s"${Game.name} is an endless 2D shooter game in which the\nplayer has to survive by killing enemies until the\nplayer is killed by an enemy.\nThe score is then determined by the time surpassed in seconds."

	val aboutGame = new Label(aboutGameStr) {
		prefWidth = 250 + (250/2)
		style = "-fx-font: 12 Regular; -fx-text-alignment: center;"
		layoutX = Global.gameWidth/2 - (250/2) - 50 - 10
		layoutY = 180+pullY
	}
	aboutGame.setTextFill(Color.web("#00BCC5"))

	val controlsText = new Label("Controls") {
		prefWidth = 250
		style = "-fx-font: 28 Regular"
		layoutX = Global.gameWidth/2 - (250/2) + 70
		layoutY = 280+pullY
	}
	controlsText.setTextFill(Color.web("#44f9ff"))

	val controlsStr = "Arrow Keys: Move around the play area\nSpace bar or Z key: Shoot bullets\nEsc: Pause Game" 

	val controls = new Label(controlsStr) {
		prefWidth = 250 + (250/3)
		style = "-fx-font: 12 Regular; -fx-text-alignment: center;"
		layoutX = Global.gameWidth/2 - (250/2) + 13
		layoutY = 320+pullY
	}
	controls.setTextFill(Color.web("#00BCC5"))

	val developersText = new Label("Developers") {
		prefWidth = 250
		style = "-fx-font: 28 Regular"
		layoutX = Global.gameWidth/2 - (250/2) + 50
		layoutY = 420+pullY
	}
	developersText.setTextFill(Color.web("#44f9ff"))

	val developersStr = "Lishan Abbas\nDavid Thingee\nLeon Kho\nDaniel Jedidiah\nYap Jia Yung" 

	val developers = new Label(developersStr) {
		prefWidth = 250
		style = "-fx-font: 12 Regular; -fx-text-alignment: center;"
		layoutX = Global.gameWidth/2 - (250/4) + 20
		layoutY = 460+pullY
	}
	developers.setTextFill(Color.web("#00BCC5"))

	content = List(back, headerText, aboutGame, controlsText, controls, developersText, developers)
}