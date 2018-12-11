package com.game.objects

import scalafx.scene.paint.Color
import scalafx.scene.canvas.GraphicsContext

class Pellet extends Drawable with Moveable {
    val _position: Position = Position(2, 2, 0)
	var _size: Double = Global.size("Pellet")
	var _speed: Double = ???
    var _rotationSpeed: Double = ???
    val _color: String = Global.color("Pellet")
    def move: Unit = ???

    def draw(drawer: GraphicsContext): Unit = {
        drawer.fill = Color.web(color)
        drawer.fillOval(position.x-size, position.y-size, size*2, size*2)
    }

    def rotateLeft: Unit = ???
    def rotateRight: Unit = ???
}