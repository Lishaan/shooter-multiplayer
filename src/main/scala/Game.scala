import scala.collection.mutable.{ArrayBuffer, Map}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.input.{KeyEvent, KeyCode}
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.animation.AnimationTimer

object Game {
	val name: String = "Shooter Multiplayer"

	/** Determines whether the current game has ended. */
	var ended: Boolean = false
}

/** A stage where a game is played on the scene until the game ends.
 *
 *  @param playerName the name of the current game's player
 */
class Game extends PrimaryStage {
	private var state: GameState = new GameState()
	private val player = new Player("Player")

	title = s"${Game.name} - Play"
	resizable = false

	scene = new Scene(Global.gameWidth, Global.gameHeight) {
		Game.ended = false

		var keys = Map (
			"Up"     -> false,
			"Right"  -> false,
			"Down"   -> false,
			"Left"   -> false
		)

		var lastTime: Long = -3
		var seconds: Double = 0.0
		Global.seconds = seconds

		// Canvas
		val canvas: Canvas = new Canvas(Global.gameWidth, Global.gameHeight);
		val drawer: GraphicsContext = canvas.graphicsContext2D

		var playerIsDead: Boolean = false

		val gameLoop: AnimationTimer = AnimationTimer(timeNow => {
			if (lastTime > 0) {
				// Delta time
				val delta = (timeNow-lastTime)/1e9
				
				player.updateBullets
				Global.playerPos = player.position
				Global.delta = delta

				// Drawings
				drawer.fill = Global.color("Background")
				drawer.fillRect(0, 0, Global.gameWidth, Global.gameHeight)
				player.draw(drawer)
				
				// Player move
				if (keys( "Up" )) player.move("Forward")
				if (keys("Down")) player.move("Backward")

				// Player rotate
				if (keys("Right")) player.rotateRight
				if (keys("Left" )) player.rotateLeft

				// println((player.position.r).toString)

				seconds += delta
				Global.seconds = seconds
				// state.update(player)
				// timerText.text = "Score: %.1f".format(seconds)
			}

			if (Game.ended) println("game ended")
			lastTime = timeNow

			// println("Fps: %.2f".format(1.0/Global.delta))
		})

		// Key pressed events
		onKeyPressed = (e: KeyEvent) => {
			e.code match {
				// Movement Controls
				case KeyCode.Up => keys("Up") = true
				case KeyCode.Right => keys("Right") = true
				case KeyCode.Down => keys("Down") = true
				case KeyCode.Left => keys("Left") = true
					
				case _ => 
			}
		}

		// Key released events
		onKeyReleased = (e: KeyEvent) => {
			e.code match {
				// Movement Controls
				case KeyCode.Up => (keys("Up") = false)
				case KeyCode.Right => (keys("Right") = false)
				case KeyCode.Down => (keys("Down") = false)
				case KeyCode.Left => (keys("Left") = false)

				case KeyCode.Space => (player.shootBullet)

				// Quitting
				case KeyCode.Q => { 
					if (Game.ended) {
						gameLoop.stop
						closeGame
					}
				}

				case _ =>
			}
		}

		content = List(canvas)
		gameLoop.start
	}

	/** Closes/ends the current game by closing the stage. */
	def closeGame = this.close

	def getGameState(): GameState = this.state

	/** Draws the menu that is displayed when a game ends.
	 *
	 *  @param drawer the graphicsContext of where the menu is drawn
	 *  @param scoreAppended a boolean value that determines whether the score appends to the highscore
	 */
	// def drawEndGameScreen(drawer: GraphicsContext, scoreAppended: Boolean): Unit = {
	// 	val fontSize = 20*Global.gameScale
	// 	drawer.fill = Global.color("PausedText")
	// 	drawer.textAlign = scalafx.scene.text.TextAlignment.Center

	// 	drawer.font = new scalafx.scene.text.Font(fontSize)
	// 	drawer.fillText("You Lose", Global.gameWidth/2, Global.playAreaHeight/2)

	// 	drawer.font = new scalafx.scene.text.Font(fontSize*0.50)
	// 	drawer.fillText("Press Q to go back to Main Menu", Global.gameWidth/2, Global.playAreaHeight/2 + (fontSize))

	// 	drawer.font = new scalafx.scene.text.Font(fontSize*0.50)
	// 	drawer.fillText("Press R to try again", Global.gameWidth/2, Global.playAreaHeight/2 + (fontSize*2))

	// 	drawer.font = new scalafx.scene.text.Font(fontSize*0.50)
	// 	drawer.fillText(if (scoreAppended) "Your score has been appended to the highscore" else "Your score did not append to the highscore", Global.gameWidth/2, Global.playAreaHeight/2 + (fontSize*3))			
	// }

	// /** Checks whether two Moveable entities are intersected.
	//  *
	//  *  @param moverA The first mover entity
	//  *  @param moverB The second mover entity
	//  *  @return a boolean value that determines whether moverA and moverB has intersected
	//  */
	// def intersected(moverA: Moveable, moverB: Moveable): Boolean = {
	// 	val dx = moverB.x - moverA.x
	// 	val dy = moverB.y - moverA.y
	// 	val dist = math.sqrt(dx*dx + dy*dy)

	// 	return dist < math.abs(moverA.size + moverB.size)
	// }
}