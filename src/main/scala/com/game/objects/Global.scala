package com.game.objects

import scala.collection.mutable.ArrayBuffer
import scala.collection.immutable.Map

import scalafx.Includes._
import scalafx.scene.paint.Color

/** A Health object that is used to store Health for all the Damageable entities.
 *
 *  @constructor create a new instance of a Health object
 *  @param max the total health of the entity
 */
case class Health (max: Double) { 
	var current: Double = max 
	
	/** 
	 *  @return the current percentage of the health as a Double
	 */
	def percentage: Double = current / max
}

/** A Position object that stores the (x, y) coordinates and rotation of a Moveable entity.
 *
 *  @constructor create a new instance of a Position object
 *  @param x the initial x coordinate
 *  @param y the initial y coordinate
 *  @param r the initial rotation angle
 */
case class Position (var x: Double, var y: Double, var r: Double = 0) {
	def rotateRight (speed: Double): Unit = { r += speed * Global.delta }
	def rotateLeft  (speed: Double): Unit = { r -= speed * Global.delta }

	def moveForward(speed: Double) = { x += speed * Math.cos(r) * Global.delta; y += speed * Math.sin(r) * Global.delta }
	def moveBackward(speed: Double) = { x -= speed * Math.cos(r) * Global.delta; y -= speed * Math.sin(r) * Global.delta }

	def moveUp    (speed: Double): Unit = { y -= speed * Global.delta }
	def moveRight (speed: Double): Unit = { x += speed * Global.delta }
	def moveDown  (speed: Double): Unit = { y += speed * Global.delta }
	def moveLeft  (speed: Double): Unit = { x -= speed * Global.delta }
}

/** A Spawner object that spawns an enemy by the given enemyName when the delay given is stopped.
 *
 *  @constructor create a new instance of a Spawner object that spawns at a range of delay
 *  @param enemyName the name of the enemy that needs to be spawn
 *  @param delayHead the starting range of the delay in seconds
 *  @param delayTail the ending range of the delay in seconds
 */
class Spawner (val enemyName: String, val delayHead: Double, val delayTail: Double) {

	/** create a new instance of a Spawner object that spawns at a consistent delay
	 *  @param enemyName the name of the enemy that needs to be spawn
	 *  @param delay the consistent delay in seconds
	 */
	def this(enemyName: String, delay: Double) = this(enemyName, delay, delay)

	private val _random: scala.util.Random = new scala.util.Random

	/** Returns a random value between the delayHead and the delayTail */
	def random: Double = (delayHead+_random.nextInt((delayTail-delayHead).toInt+1))

	/** The delay counter of the [[Spawner]] object */
	var counter: Double = random
	
	/** Returns a boolean value that determines whether the counter has been stopped */
	def stopped: Boolean = (counter <= 0)

	/** Resets the counter to a random value between the delayHead and delatTail */
	def reset: Unit = { counter = random }

	/** Updates the current counter of the spawner by the global delta time */
	def update: Unit = { counter -= Global.delta }
}

/** A Global static object that stores all the variables that are needed in the game every iteration of the game loop. */
object Global {
	val playAreaHeight: Double = Global.gameHeight/1.5

	/** The current delta time difference */
	var delta: Double = 0

	/** The scale of the game */
	val gameScale: Double = 1.2

	/** The speed of the game */
	val gameSpeed: Double = 1.0

	/** The width of the game scene */
	val gameWidth: Double = 600

	/** The height of the game scene */
	val gameHeight: Double = 600

    val rotationSpeed: Map[String, Double] = Map (
        "Player" -> 0.00872664626 // (pi/360)
    )

	/** The intiial sizes of the Moveable entities in the game */
	val size: Map[String, Double] = Map (
		"Player"         -> 20,
		"Bullet"         -> 4,
		"Pellet"		 -> 4
	)

	/** The intiial speeds of the Moveable entities in the game */
	val speed: Map[String, Double] = Map (
		"Player"         -> 170,
		"Bullet"         -> 500
	)

	/** The health of all the Damageable entities */
	val health: Map[String, Double] = Map (
		"Player"  -> 50
	)

	/** The colors of all the Drawable entities in the game */
	val color: Map[String, String] = Map (
		"Background"    -> "041A1A",
		"TimerText"     -> "FBFBFB",
		"PausedText"    -> "FBFBFB",
		"Player"        -> "FBFBFB",
		"Player-alt"    -> "6F997A",
		"Bullet"        -> "44f9ff",
		"Pellet"        -> "44f9ff"
		/* Link: https://coolors.co/0c0910-cdd1c4-5c80bc-6b2737-1d7874 */
	)
}