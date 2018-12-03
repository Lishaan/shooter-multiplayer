import scalafx.Includes._
import scalafx.scene.paint.Color
import scalafx.scene.canvas.GraphicsContext

object Player {
    def generateID(): String = { scala.util.Random.alphanumeric.take(10).mkString }
}

/** A Player object controlled by the user that inherits the traits: [[Drawable]], [[Moveable]] and [[Shootable]].
 *
 *  @constructor create a new instance of a Player object by the given player name
 *  @param playerName the name of the Player
 */
class Player (playerName: String) extends Drawable with Moveable with Shootable {
	/** create a new instance of a Player object with the name "Player" */
    def this() = this("Player")

    val ID: String = Player.generateID()

    private val _name: String = playerName
	private var _kills: Int = 0

	val _position: Position = new Position(Global.gameWidth/2, Global.gameHeight-50)
	var _size: Double = Global.size("Player")
	val _color: Color = Global.color("Player")
	var _speed: Double = Global.speed("Player")
    var _rotationSpeed: Double = 0

	def shootBullet: Unit = {
		_bullets +:= new Bullet(this.position)
	}

	def move = println("Error: Parameter (direction: String) required")

	/** Moves the player at the given direction 
     *  @param direction the direction as a String
	 */
	def move(direction: String): Unit = {
		speed = Global.speed("Player")
		size = Global.size("Player")

		if (direction.equals("Up") && (position.y-size > 0)) {
			position.moveUp(speed)
		} else if (direction.equals("Right") && (position.x+size < Global.gameWidth)) {
			position.moveRight(speed)
		} else if (direction.equals("Down") && (position.y+size < Global.gameHeight)) {
			position.moveDown(speed)
		} else if (direction.equals("Left") && (position.x-size > 0)) {
			position.moveLeft(speed)
		}
	}

    // TODO
    def rotateLeft: Unit = Unit
    def rotateRight: Unit = Unit

	def draw(drawer: GraphicsContext): Unit = {
		// Draws at center
		drawer.fill = color
		drawer.fillOval(position.x-size, position.y-size, size*2, size*2)
	}

	def name = _name
	def kills = _kills

	/** Increments the player's kills. */
	def incrementKills = { _kills = _kills + 1 }
}