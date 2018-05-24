package marcos.akka.application.general

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.Patterns
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

@Service
class GeneralService{

    @Autowired
    private lateinit var actorSystem: ActorSystem
    @Autowired
    private lateinit var springExtension: SpringExtension

    fun simpleCallLocalActor(){
        val props: Props = springExtension.get(actorSystem).localActorProps("localActor", "ator1")
        val ator1: ActorRef = actorSystem.actorOf(props)
        ator1.tell(MessageCommand(MessageType.MESSAGE, "Ola!"), ator1)
    }

    fun callRemoteActor(): Mono<RemoteResponseMessageCommand> {

        val props: Props = springExtension.get(actorSystem).localActorProps("localActor", "atorLocal1")
        val atorLocal1: ActorRef = actorSystem.actorOf(props)
        val message = RemoteRequestMessageCommand(MessageType.MESSAGE, "Ola, remoto!")
        return toMono(Patterns.ask(atorLocal1, message, timeout))
    }
}