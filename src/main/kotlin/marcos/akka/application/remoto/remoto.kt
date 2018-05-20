package marcos.akka.application.remoto

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.Patterns.*
import marcos.akka.configs.SpringExtension
import marcos.akka.configs.toMono
import marcos.akka.extensions.timeout
import marcos.akka.model.MessageCommand
import marcos.akka.model.MessageType
import marcos.akka.model.RemoteRequestMessageCommand
import marcos.akka.model.RemoteResponseMessageCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import scala.concurrent.Future

@Service
class RemoteService{

    @Autowired
    private lateinit var actorSystem: ActorSystem
    @Autowired
    private lateinit var springExtension: SpringExtension

    fun callRemoteActor(): Mono<RemoteResponseMessageCommand> {

        val props: Props = springExtension.get(actorSystem).remoteActorProps("localActor", "atorLocal1")
        val atorLocal1: ActorRef = actorSystem.actorOf(props)
        val message = RemoteRequestMessageCommand(MessageType.MESSAGE, "Ola, remoto!")
        return toMono(ask(atorLocal1, message, timeout))
    }
}