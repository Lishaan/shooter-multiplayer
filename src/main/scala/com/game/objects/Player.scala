package com.game.objects

import scalafx.Includes._
import scalafx.scene.paint.Color
import scalafx.scene.canvas.GraphicsContext

import akka.actor.ActorRef

/** A Player object controlled by the user that inherits the traits: [[Drawable]], [[Moveable]] and [[Shootable]].
 *
 *  @constructor create a new instance of a Player object by the given player name
 *  @param playerName the name of the Player
 */
class Player (private val _ID: Int) extends Drawable with Moveable with Shootable with Damageable with Serializable {
	private val _name: String = "Player"

	val _position: Position = new Position(Global.gameWidth/2, Global.gameHeight/2, 0)
	val _health: Health = new Health(100)
	var _size: Double = Global.size("Player")
	val _color: String = Global.color("Player")
	var _speed: Double = Global.speed("Player")
    var _rotationSpeed: Double = 4

	def shootBullet: Unit = (_bullets +:= new Bullet(this.position))

	def move = println("Error: Parameter (direction: String) required")

	/** Moves the player at the given direction 
     *  @param direction the direction as a String
	 */
	def move(direction: String): Unit = {
		speed = Global.speed("Player")
		size = Global.size("Player")

		// TODO: Bounds
		// val outOfBounds: Boolean = (
		// 	((position.x+size < Global.gameWidth) && (position.y+size < Global.gameHeight))
		// 	||
		// 	((position.y-size > 0) && (position.x-size > 0))
		// )

		// println(outOfBounds)

		// // Normalized Angle from -90 to 90
		// val na: Double = (Math.sin(position.r) * 90)

		direction match {
			case "Forward"  => position.moveForward(speed)
			case "Backward" => position.moveBackward(speed)
			case _ =>
		}
	}

    def rotateLeft: Unit  = position.rotateLeft(rotationSpeed)
    def rotateRight: Unit = position.rotateRight(rotationSpeed)

	def draw(drawer: GraphicsContext): Unit = {
		// Draw health bar
		drawer.fill = Color.Blue
		drawer.fillRect(position.x-size, position.y+size+(size/2), size*2, size/4)

		drawer.fill = Color.Red
		drawer.fillRect(position.x-size, position.y+size+(size/2), (size*2)*health.percentage, size/4)

		// Draw bullets
		bullets.foreach(_.draw(drawer))

		// Draws at center
		drawer.fill = Color.web(color)
		drawer.fillOval(position.x-size, position.y-size, size*2, size*2)

		// Draw gun
		val x = position.x - (size/4)
		val y = position.y - (size/4)
		val r = position.r

		val gun_x: Double = x + ((size) * Math.cos(r))
		val gun_y: Double = y + ((size) * Math.sin(r))

		drawer.fill = Color.web("ff0000")
		drawer.fillOval(gun_x, gun_y, size/2, size/2)
	}

	def ID = _ID
	def name = _name

	override def toString(): String = {
		val x: Double = position.x
		val y: Double = position.y
		val r: Double = position.r
		val h: Double = health.current

		return s"ID: $ID, Position: ($x, $y, $r), health: $h"
	}
}