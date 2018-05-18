package marcos.akka.cluster

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClusterApplication

fun main(args: Array<String>) {
    runApplication<ClusterApplication>(*args)
}
