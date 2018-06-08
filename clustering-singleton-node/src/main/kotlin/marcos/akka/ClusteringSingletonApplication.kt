package marcos.akka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClusteringSingletonApplication

fun main(args: Array<String>) {
    runApplication<ClusteringSingletonApplication>(*args)
}
