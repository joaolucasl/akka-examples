package marcos.akka

import marcos.akka.actor.local.LocalActor
import marcos.akka.model.NodeType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GenericLocalApplication

fun main(args: Array<String>) {
    LocalActor.nodeType = NodeType.valueOf(args[0])
    runApplication<GenericLocalApplication>(*args)
    for (i in 1..10) println("")
}
