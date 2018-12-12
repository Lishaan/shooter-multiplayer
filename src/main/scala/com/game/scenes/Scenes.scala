package com.game.scenes

import scala.collection.immutable.Map

import scalafx.scene.paint.Color

object Scenes {
	/** The colors of the components in the Main Menu */
	val color: Map[String, Color] = Map(
		"Background" -> Color.web("000D0D")
	)

	/** The fx css stylings of the buttons in the Main Menu */
	val buttonStyle: Map[String, String] = Map(
		"Normal"      -> "-fx-font-weight: bold; -fx-background-color: #004E52; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: #44f9ff;",
		"Smaller"     -> "-fx-font-weight: bold; -fx-background-color: #004E52; -fx-background-radius: 50; -fx-font-size: 20; -fx-text-fill: #44f9ff;",
		"onEntered"   -> "-fx-font-weight: bold; -fx-background-color: #003133; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: #44f9ff;",
		"onExited"    -> "-fx-font-weight: bold; -fx-background-color: #004E52; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: #44f9ff;",
		"onAction"    -> "-fx-font-weight: bold; -fx-background-color: #003133; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: #44f9ff; -fx-rotate: 5",
		"onAction-sm" -> "-fx-font-weight: bold; -fx-background-color: #003133; -fx-background-radius: 50; -fx-font-size: 24; -fx-text-fill: #44f9ff; -fx-rotate: 5"
	)
}