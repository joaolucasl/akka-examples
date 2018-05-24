package marcos.akka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShardingRemoteApplication

fun main(args: Array<String>) {
    runApplication<ShardingRemoteApplication>(*args)
}
