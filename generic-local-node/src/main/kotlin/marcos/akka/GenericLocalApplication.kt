package marcos.akka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GenericLocalApplication

fun main(args: Array<String>) {
    runApplication<GenericLocalApplication>(*args)
}
