package marcos.akka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AkkaHttpApplication

fun main(args: Array<String>) {
    runApplication<AkkaHttpApplication>(*args)
}
