package marcos.akka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext

@SpringBootApplication
class GenericLocalApplication

fun main(args: Array<String>) {
    val context:ConfigurableApplicationContext= runApplication<GenericLocalApplication>(*args)
}
