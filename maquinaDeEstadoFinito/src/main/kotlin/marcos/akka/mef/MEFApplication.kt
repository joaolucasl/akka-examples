package marcos.akka.mef

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MEFApplication

fun main(args: Array<String>) {
    runApplication<MEFApplication>(*args)
}
