import scala.collection.mutable.{ArrayBuffer, Map}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.text.Text
import scalafx.scene.paint.Color
import scalafx.scene.input.{KeyEvent, KeyCode}
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.event.ActionEvent
import scalafx.animation.AnimationTimer

object Game {
	val name: String = "Spherical Insanity"

	private val highscoresFilePath: String = System.getProperty("java.io.tmpdir") + "/highscores.txt"

	val highscoresDir: String = System.getProperty("java.io.tmpdir") + "/"

	/** Determines whether the current game has ended. */
	var ended: Boolean = false

	/** Toggles the pausing of the game. */
	// def togglePause: Unit = { Game.paused = !Game.paused }
}

/** A stage where a game is played on the scene until the game ends.
 *
 *  @param playerName the name of the current game's player
 */
class Game extends PrimaryStage {
	var state: GameState = new GameState()
	private val player = new Player("Player")

	title = s"${Game.name} - Play"
	resizable = false

	scene = new Scene(Global.gameWidth, Global.gameHeight) {
		Game.ended = false

		// val spawners: ArrayBuffer[Spawner] = ArrayBuffer (
		// 	new Spawner("Bouncer", 12),
		// 	new Spawner("Seeker" , 2.5, 5.0),
		// 	new Spawner("Shooter", 30.0, 40.0)
		// )

		// var enemies: ArrayBuffer[Enemy] = ArrayBuffer()
		
		// var timerText = new Text(10, 20, "Score: 0.0") {
		// 	fill = Global.color("TimerText")
		// }

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

		// var didAppend: Boolean = false
		var playerIsDead: Boolean = false

		val gameLoop: AnimationTimer = AnimationTimer(timeNow => {
			if (lastTime > 0) {
				// Delta time
				val delta = (timeNow-lastTime)/1e9
				
				player.updateBullets
				Global.playerPos = player.position
				Global.delta = delta

				// Enemies Spawn
				// spawners.foreach(delay => {
				// 	delay.update
				// 	if (delay.stopped) {
				// 		enemies +:= Enemy.spawn(delay.enemyName)
				// 		delay.reset
				// 	}
				// })

				// Drawings
				drawer.fill = Global.color("Background")
				drawer.fillRect(0, 0, Global.gameWidth, Global.gameHeight)

				player.bullets.foreach(b => b.draw(drawer))
				player.draw(drawer)
				// enemies.foreach(e => e.draw(drawer))

				// Enemies
				// if (!enemies.isEmpty) {
				// 	var indexes: ArrayBuffer[Int] = ArrayBuffer()
				// 	for (i <- 0 until enemies.length) {
						
				// 		// Player death from intersecting with enemies
				// 		playerIsDead = intersected(enemies(i), player)

				// 		// Player death from Shooter's bullets
				// 		if (enemies(i).isInstanceOf[Shooter]) {
				// 			enemies(i).asInstanceOf[Shooter].shootBullet
				// 			enemies(i).asInstanceOf[Shooter].updateBullets
				// 			enemies(i).asInstanceOf[Shooter].bullets.foreach(bullet => {
				// 				playerIsDead = intersected(player, bullet)
				// 			})
				// 		}

				// 		// Player death
				// 		if (playerIsDead) {

				// 			// Highscores
				// 			if (Global.appendToHighscoresFile)
				// 				didAppend = Highscores.append(new Score(player.name, player.kills, seconds))

				// 			Game.ended = true
				// 			timer.stop
				// 		}

				// 		// Enemy death from Player's bullets
				// 		player.bullets.foreach(bullet => {
				// 			if (intersected(bullet, enemies(i))) {
				// 				enemies(i).inflictDamage(bullet.damage)
				// 				bullet.remove
								
				// 				if (enemies(i).dead) {
				// 					enemies(i).remove
				// 					player.incrementKills
				// 					if (!indexes.contains(i)) indexes += i
				// 				}
				// 			}
				// 		})

				// 		// Enemies move
				// 		enemies(i).move
				// 	}
				// 	indexes.foreach(index => enemies.remove(index))
				// }
				
				// Player move
				if (keys( "Up" )) player.move("Forward")
				if (keys("Down")) player.move("Backward")

				// Player rotate
				if (keys("Right")) player.rotateRight
				if (keys("Left" )) player.rotateLeft

				// println((player.position.r).toString)

				// Game speed configuration
				// if (Global.appendToHighscoresFile || (Global.gameScale==1.2 && Global.gameSpeed==1.0)) {					
				// 	if (seconds >= 10 ) Global.gameSpeed = 1.1
				// 	if (seconds >= 40 ) Global.gameSpeed = 1.2
				// 	if (seconds >= 70 ) Global.gameSpeed = 1.3
				// 	if (seconds >= 110) Global.gameSpeed = 1.4
				// 	if (seconds >= 160) Global.gameSpeed = 1.5
				// 	if (seconds >= 220) Global.gameSpeed = 1.6
				// 	if (seconds >= 290) Global.gameSpeed = 1.7
				// 	if (seconds >= 360) Global.gameSpeed = 1.8
				// 	if (seconds >= 450) Global.gameSpeed = 1.9
				// 	if (seconds >= 550) Global.gameSpeed = 2.0
				// }

				seconds += delta
				Global.seconds = seconds
				// gameState.update(player)
				// timerText.text = "Score: %.1f".format(seconds)
			}

			// if (Game.paused) drawPausedScreen(drawer)
			// if (Game.ended) drawEndGameScreen(drawer, didAppend)
			if (Game.ended) println("game ended")
			lastTime = timeNow
			Global.updateStats

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