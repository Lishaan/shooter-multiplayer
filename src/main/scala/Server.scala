class Server extends App {
    println("Server")
    val gameState: GameState = new Game().getGameState()
    gameState.print()
    // TODO: 
    // while (server is running) {
    //     for (client <- clients) {
    //         gameState.update with (client.playerObject)
    //     }

    //     for (client <- clients) {
    //         client.updateGameState with (gameState)
    //     }
    // }
}