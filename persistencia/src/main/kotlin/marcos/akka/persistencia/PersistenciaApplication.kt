package marcos.akka.persistencia

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PersistenciaApplication

fun main(args: Array<String>) {
    runApplication<PersistenciaApplication>(*args)
}
