import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.text.Text

class Client extends JFXApp {
	private val gameObject: Game = new Game()
	
	stage = gameObject

	def getGameState(): GameState = this.gameObject.getGameState
}