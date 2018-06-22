package marcos.akka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AgendamentoApplication

fun main(args: Array<String>) {
    runApplication<AgendamentoApplication>(*args)
    for (i in 1..10) println("")
}
