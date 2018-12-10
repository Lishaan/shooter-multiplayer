import scala.io.StdIn

import java.net.{NetworkInterface, InetAddress}

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._

import com.game.net.Client._
import com.game.net.Server._
import com.game.objects.{Game, GameState}
import com.game.net.{Client, Server}

import scalafx.Includes._
import scalafx.application.{JFXApp, Platform}
import scalafx.application.JFXApp.PrimaryStage

object App {
    def main(args: Array[String]): Unit = {
        println("Shooter Multiplayer")
        println("1. Client")
        println("2. Server-Client")
        println("3. Exit")
        val choice: Int = getValidInput()

        if (!((choice >= 1) && (choice <= 2))) {
            sys.exit(0)
        }

        val ip: String = getIpFromList()
        var port: String = "0"
        // if (choice equals 2) serverport = StdIn.readLine("Enter Port: ")
        
        val system = ActorSystem("shooter", getConfig(ip, port))
        val clientRef = system.actorOf(Props[Client], "client")
        val serverRef = system.actorOf(Props[Server], "server")

        val game: Game = new Game(system, serverRef, clientRef)
        
        println("Connect to server")
        val connectIP: String = StdIn.readLine("Enter IP: ")
        val connectPort: String = StdIn.readLine("Enter Port: ")

        clientRef ! StartJoin(connectIP, connectPort)

        if (choice equals 2) {
            StdIn.readLine("Press Enter to Start Server")
            serverRef ! Server.Start
        }

        // Platform.runLater(game)
        game.main(args)
    }

    private def getIpFromList(): String = {
        var count = 0
        val addresses: Map[Int, InetAddress] = (for (inf <- NetworkInterface.getNetworkInterfaces.asScala; add <- inf.getInetAddresses.asScala; if (!add.toString.contains("%"))) yield { 
            count += 1; (count -> add) 
        }).toMap

        println("\nAvailable Addresses")
        for ((i, add) <- addresses) {
            println(s"$i: $add")
        }

        println("Please select which interface to bind")
        var choice: Int = 0
        do {
            choice = StdIn.readInt()
        } while ((choice < 0) && (choice >= addresses.size))

        return addresses(choice).getHostAddress
    }

    private def getValidInput(): Int = {
        val invalid = (x: Int) => (x != 1) && (x != 2) && (x != 3)
        var exceptionThrown = false
        var input: Int = Int.MaxValue

        while (invalid(input)) {
            try {
                input = StdIn.readLine("Choice: ").toInt
            } catch {
                case _: NumberFormatException => exceptionThrown = true
            }

            if (invalid(input) || exceptionThrown) println("Enter a valid input") else return input
        }

        return 1
    }

    private def getConfig(hostname: String, port: String = "0"): Config = {
        println("Binding to: " + hostname)
        val config = ConfigFactory.parseString(s"""
        |  akka {
        |    loglevel = "INFO"
        |  
        |    actor {
        |      provider = "akka.remote.RemoteActorRefProvider"
        |      serializers {
        |          java = "akka.serialization.JavaSerializer"
        |          proto = "akka.remote.serialization.ProtobufSerializer"
        |          custom = "com.game.serialization.CustomSerializer"
        |      }
        |  
        |      serialization-bindings {
        |          "com.game.objects.GameState" = custom
        |      }
        |    }
        |  
        |    remote {
        |      enabled-transports = ["akka.remote.netty.tcp"]
        |      netty.tcp {
        |        hostname = "${hostname}"
        |        port = 0             
        |      }
        |  
        |      log-sent-messages = on
        |      log-received-messages = on
        |    }
        |  
        |  }
        |  
        """.stripMargin)

        return config.withFallback(ConfigFactory.load())
    }
}