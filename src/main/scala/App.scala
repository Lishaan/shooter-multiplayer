import scala.io.StdIn

import java.net.NetworkInterface

import Client._
import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._

object App extends App {
    var count = -1
    val addresses = (for (inf <- NetworkInterface.getNetworkInterfaces.asScala;
        add <- inf.getInetAddresses.asScala) yield {
            count = count + 1
            (count -> add)
        }
    ).toMap

    println(addresses)

    for ((i, add) <- addresses){
        println(s"$i = $add")
    }

    println("please select which interface to bind")
    var selection: Int = 0
    do {
        selection = scala.io.StdIn.readInt()
    } while (!(selection >= 0 && selection < addresses.size))

    val ipaddress = addresses(selection)

    val overrideConf = ConfigFactory.parseString(s"""
    |akka {
    |  loglevel = "INFO"
    |
    |  actor {
    |    provider = "akka.remote.RemoteActorRefProvider"
    |  }
    |
    |  remote {
    |    enabled-transports = ["akka.remote.netty.tcp"]
    |    netty.tcp {
    |      hostname = "${ipaddress.getHostAddress}"
    |      port = 0
    |    }
    |
    |    log-sent-messages = on
    |    log-received-messages = on
    |  }
    |
    |}
    |
    """.stripMargin)

    val myConf = overrideConf.withFallback(ConfigFactory.load())
    val system = ActorSystem("shooter", myConf)
    
    //create server actor
    val serverRef = system.actorOf(Props[Server], "server")
    
    //create client actor
    val clientRef = system.actorOf(Props[Client], "client")

    val ip: String = StdIn.readLine("Enter IP: ")
    val port: String = StdIn.readLine("Enter Port: ")

    clientRef ! StartJoin(ip, port)

    val myIP = ipaddress.toString().substring(1,ipaddress.toString().size)
    //println("Your ip: " + ipAdd)

    if (ip == myIP) {
        StdIn.readLine("Press any to begin!")
        serverRef ! Server.Start
    }
}