package marcos.akka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClusteringSeedNodeApplication

fun main(args: Array<String>) {
    runApplication<ClusteringSeedNodeApplication>(*args)
}
