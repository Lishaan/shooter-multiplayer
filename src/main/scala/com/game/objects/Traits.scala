package com.game.objects

import scala.collection.mutable.ArrayBuffer

import scalafx.Includes._
import scalafx.scene.paint.Color
import scalafx.scene.canvas.GraphicsContext

/** A trait that defines an entity that can shoot an [[Ammo]] object. */
trait Shootable {
	protected var _bullets: ArrayBuffer[Ammo] = ArrayBuffer[Ammo]()

	/** The ammo array that stores all the ammo currently in the game scene. */
	def bullets: ArrayBuffer[Ammo] = _bullets

	/** Manages the bullets array by removing bullets when they go out of the game scene. */
	def updateBullets: Unit = {

		// Checks if the bullets array is empty or not
		if (!bullets.isEmpty) {
			// Creates a buffer that stores all the indexes of the bullets in the bullets array that has to be removed
			var indexes: ArrayBuffer[Int] = ArrayBuffer[Int]()

			// Bullets move
			for (i <- 0 until bullets.length) {
				bullets(i).move

				// If the bullet is out of the game scene, add the index to the indexes buffer
				if (bullets(i).y > Global.gameHeight-bullets(i).size || bullets(i).y < bullets(i).size) {
					if (!indexes.contains(i)) indexes += i
				}
			}

			// Remove all bullets that is out of the game scene
			indexes.foreach(index => if (bullets.length <= index) bullets.remove(index))
		}
	}

	/** Defines how and when to shoot an Ammo. */
	def shootBullet: Unit
}

/** A trait that defines an entity that can be drawn into the game scene. */
trait Drawable {
	protected var _color: String

	/** The color of the [[Drawable]] entitiy. */
	def color: String = _color

	def color_=(c: String) = _color = c

	/** Defines how the entity is drawn.
	 *
	 *  @param drawer the GraphicsContext of where the entity gets drawn
	 */
	def draw(drawer: GraphicsContext): Unit
}

/** A trait that defines an entity that can move around the game scene. */
trait Moveable {
	protected val _position: Position
	protected var _size: Double
	protected var _speed: Double
    protected var _rotationSpeed: Double

	/** The current position of the Moveable entity */
	def position: Position = _position

	/** The current size of the Moveable entity */
	def size: Double = _size

	/** The current speed of the Moveable entity */
	def speed: Double = _speed

	def rotationSpeed: Double = _rotationSpeed
	
	/** The current x coordinate of the Moveable entity */
	def x: Double = _position.x

	/** The current y coordinate of the Moveable entity */
	def y: Double = _position.y

	def size_=(size: Double) = { _size = size }
	def speed_=(speed: Double) = { _speed = speed }
	def rotationSpeed_=(rotationSpeed: Double) = { _rotationSpeed = rotationSpeed }

	/** Defines how the entity moves around the game scene. */
	def move: Unit

    def rotateLeft: Unit
    def rotateRight: Unit

	/** Removes the entity from the game scene. */
	def remove: Unit = {
		position.x = -(800 * 800)
		position.y = (800 * 800)
		position.r = -1.5
	}
}

/** A trait that defines an entity that can be damaged. */
trait Damageable {
	protected val _health: Health

	/** Checks whether the damageable entity is dead. */
	def dead: Boolean = (_health.current <= 0)

	/** Checks whether the damageable entity is alive. */
	def alive: Boolean = !dead

	/** Returns the current health object of the Damageable entity */
	def health: Health = _health

	/** Deducts health by the given damage.
	 *
	 *  @param damage the damage given
	 */
	def inflictDamage(damage: Double): Unit = (health.current -= damage)
}