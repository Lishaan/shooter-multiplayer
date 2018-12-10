package com.game.objects

import scala.collection.mutable.ArrayBuffer

import java.io.{ObjectInputStream, ByteArrayInputStream}

import util.control.Breaks._

object GameState {
    def parseFrom(bytes: Array[Byte]): GameState = {
        val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
        val value = ois.readObject
        ois.close()

        return value.asInstanceOf[GameState]
    }
}

class GameState extends Serializable {
    // private val _intersectedPlayerIDs: ArrayBuffer[(String, String)] = ArrayBuffer[(String, String)]()
    private val _players: ArrayBuffer[Player] = new ArrayBuffer[Player]()

    def addPlayer(player: Player): Unit = (players += player)

    def getPlayerByID(ID: Int): Player = {
        var player: Player = null

        for (p <- players; if (p.ID == ID)) {
            println(s"FOUND PLAYER ${p.toString()}")
            player = p
        }

        if (player == null) {
            println("GAME STATE UPDATE ERROR")
            return new Player(1)
        } else {
            println("UPDATED GAME STATE")
            return player
        }
    }

    def update(playerToUpdate: Player): Unit = {
        var indexToUpdate = 0

        breakable {
            for (index <- 0 until players.length) {
                if (players(index).ID == playerToUpdate.ID) {
                    indexToUpdate = index
                    break
                }
            }
        }

        players(indexToUpdate) = playerToUpdate
    }

    def print(): Unit = {
        println("GAMESTATE -> Players:")
        players.foreach(player => println(player.toString))
    }

    def players = _players
}