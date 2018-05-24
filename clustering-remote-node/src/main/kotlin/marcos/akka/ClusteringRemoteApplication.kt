package marcos.akka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClusteringRemoteApplication

fun main(args: Array<String>) {
    runApplication<ClusteringRemoteApplication>(*args)
}
