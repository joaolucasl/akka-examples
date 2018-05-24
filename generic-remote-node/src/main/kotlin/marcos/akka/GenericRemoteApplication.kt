package marcos.akka

import marcos.akka.actors.ActorCreator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext

@SpringBootApplication
class GenericRemoteApplication

fun main(args: Array<String>) {
    val context:ConfigurableApplicationContext= runApplication<GenericRemoteApplication>(*args)
    val actorCreator:ActorCreator = context.getBean("actorCreator") as ActorCreator
    actorCreator.createAllActors()

}
