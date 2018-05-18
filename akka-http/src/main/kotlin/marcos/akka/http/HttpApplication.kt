package marcos.akka.http

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HttpApplication

fun main(args: Array<String>) {
    runApplication<HttpApplication>(*args)
}
