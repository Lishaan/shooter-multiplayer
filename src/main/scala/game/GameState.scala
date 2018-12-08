package game

import scala.collection.mutable.ArrayBuffer

import java.io.{ObjectInputStream, ByteArrayInputStream}

object GameState {
    def parseFrom(bytes: Array[Byte]): GameState = {
        new GameState()
        // val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
        // val value = ois.readObject
        // ois.close()

        // return value.asInstanceOf[GameState]
    }
}

class GameState extends Serializable {
    // private val _intersectedPlayerIDs: ArrayBuffer[(String, String)] = ArrayBuffer[(String, String)]()
    private val _players: ArrayBuffer[Player] = new ArrayBuffer[Player]()

    // def addPlayer(player: Player): Unit = (players += player)

    // def players = _players

    // def findIntersectedPlayerIDs(): ArrayBuffer[(String, String)] = {
    //     new ArrayBuffer[(String, String)]() // TODO
    // }

    // def update(player: Player): Unit = {
    //     // this.players.foreach(p => {
    //     //     if (p.ID equals player.ID) {
    //     //         p.copy(player)
    //     //     }
    //     // })
    //     // this.intersectedPlayerIDs = findIntersectedPlayerIDs()
    // }

    // def update(players: ArrayBuffer[Player]): Unit = {
    //     // this.players = players
    //     // this.intersectedPlayerIDs = findIntersectedPlayerIDs()
    // }

    // def print(): String = s"GameState"

//     def getGameState(): GameState = return this
}