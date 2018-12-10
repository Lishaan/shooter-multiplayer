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
    private val _players: ArrayBuffer[Player] = new ArrayBuffer[Player]()

    def addPlayer(player: Player): Unit = (players += player)

    def playersExcept(player: Player): ArrayBuffer[Player] = {
        val out: ArrayBuffer[Player] = ArrayBuffer[Player]()
        for (p <- players; if (p.ID != player.ID)) (out += p)

        return out
    }

    def getPlayerByID(ID: Int): Player = {
        var player: Player = null

        for (p <- players; if (p.ID == ID)) {
            player = p
        }

        if (player == null) {
            println("GAME STATE UPDATE ERROR")
            return new Player(Int.MinValue)
        } else {
            return player
        }
    }

    def getPlayerIndexByID(playerID: Int): Int = {
        var index: Int = 0
        breakable {
            for (player <- players) {
                if (player.ID == playerID) {
                    break
                } else {
                    index += 1
                }
            }
        }

        return index
    }

    def getPlayerByIndex(index: Int): Player = {
        if (index < players.length) {
            return players(index)
        } else {
            return new Player(Int.MinValue)
        }
    }

    def updateIntersections(): Unit = {
        // Game logic
        for (p1 <- players; p2 <- players; if (p1.ID != p2.ID)) {
            p1.bullets.foreach(bullet => {
                if (Game.intersected(p2, bullet)) {
                    p2.inflictDamage(bullet.damage)
                    bullet.remove
                }
            })
            p2.bullets.foreach(bullet => {
                if (Game.intersected(p1, bullet)) {
                    p1.inflictDamage(bullet.damage)
                    bullet.remove
                }
            })
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

        if (indexToUpdate < players.length) {
            players(indexToUpdate) = playerToUpdate
        }
    }

    def print(): Unit = {
        println("GAMESTATE: Players:")
        players.foreach(player => println(player.toString))
    }

    def players = _players
}