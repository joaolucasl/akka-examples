package marcos.akka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AkkaExamplesApplication

fun main(args: Array<String>) {
    runApplication<AkkaExamplesApplication>(*args)
}
