import scala.collection.mutable.ArrayBuffer

class GameState extends Serializable {
    var intersectedPlayerIDs: ArrayBuffer[(String, String)] = ArrayBuffer[(String, String)]()
    var players: ArrayBuffer[Player] = new ArrayBuffer[Player]()

    def findIntersectedPlayerIDs(): ArrayBuffer[(String, String)] = {
        new ArrayBuffer[(String, String)]() // TODO
    }

    def update(player: Player): Unit = {
        // var addPlayer: Player = 
        // this.players.foreach(p => {
        //     if (p.ID equals player.ID) {
                
        //     }
        // })
        this.intersectedPlayerIDs = findIntersectedPlayerIDs()
    }

    def update(players: ArrayBuffer[Player]): Unit = {
        this.players = players
        this.intersectedPlayerIDs = findIntersectedPlayerIDs()
    }

    def print(): String = s"GameState"

    def getGameState(): GameState = return this
}