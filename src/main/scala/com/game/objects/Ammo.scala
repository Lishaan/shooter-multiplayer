package com.game.objects

import scalafx.Includes._
import scalafx.scene.paint.Color
import scalafx.scene.canvas.GraphicsContext

/** A generalized superclass for all the [[Shootable]] entities. */
abstract class Ammo extends Drawable with Moveable {
	protected val _damage: Double = 5

	/** The damage of the Ammo object */
	def damage: Double = _damage
}

/** A bullet that can be shot from the position it was created. This bullet will move upwards on the game scene.
 *
 *  @constructor create a new bullet with a position
 *  @param playerPos the initial position of the bullet
 */
class Bullet (playerPos: Position) extends Ammo with Serializable {
	val _position: Position = new Position(playerPos.x, playerPos.y, playerPos.r)
	var _speed: Double = Global.speed("Bullet")
    var _rotationSpeed: Double = 0
	var _size: Double  = Global.size("Bullet")
	var _color: String = Global.color("Bullet")

	def move = { 
		speed = Global.speed("Bullet")
		size = Global.size("Bullet")
		position.moveForward(speed)
	}

	def draw(drawer: GraphicsContext): Unit = {
		// Draws at center
		drawer.fill = Color.web(color)
		drawer.fillOval(position.x-size, position.y-size, size*2, size*2)
	}

    def rotateLeft: Unit = ???
    def rotateRight: Unit = ???
}