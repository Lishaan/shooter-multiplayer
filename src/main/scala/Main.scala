import scala.io.StdIn

object Main extends App {
    println("ShooterMultiplayer menu")
    println("1. Server")
    println("2. Client")
    println("3. Exit")

    val input: Int = getValidInput()

    input match {
        case 1 => new Server().main(args)
        case 2 => new Client().main(args)
        case 3 | _ => println("Exiting Application")
    }

    private def getValidInput(): Int = {
        val invalid = (x: Int) => (x != 1) && (x != 2) && (x != 3)
        var exceptionThrown = false
        var input: Int = Int.MaxValue

        do {
            try {
                println("Choice: ")
                input = StdIn.readInt
            } catch {
                case _: NumberFormatException => exceptionThrown = true
            }

            if (invalid(input) || exceptionThrown) {
                println("Enter a valid input") 
            } else {
                return input
            }
        } while (invalid(input))

        return 1
    }
}