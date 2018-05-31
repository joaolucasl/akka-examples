package marcos.akka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AgendamentoRemoteApplication

fun main(args: Array<String>) {
    runApplication<AgendamentoRemoteApplication>(*args)
}
