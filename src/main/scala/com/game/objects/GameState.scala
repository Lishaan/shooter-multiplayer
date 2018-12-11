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
    private var _players: Array[Player] = Array[Player](new Player(1), new Player(2))

    def getPlayerByID(id: Int): Player = {
        id match {
            case 1 => return players(0)
            case 2 => return players(1)

            case _ => return new Player(-1)
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
        var indexToUpdate: Int = 0

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