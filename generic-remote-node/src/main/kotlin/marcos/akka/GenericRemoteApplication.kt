package marcos.akka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GenericRemoteApplication

fun main(args: Array<String>) {
    runApplication<GenericRemoteApplication>(*args)
    for (i in 1..10) println("")
}
