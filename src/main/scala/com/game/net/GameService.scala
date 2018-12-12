package com.game.net

import java.net.InetAddress

object GameService {
    val HOSTNAME: String = InetAddress.getLocalHost.getHostAddress
    val PORT: String = "55556"
}