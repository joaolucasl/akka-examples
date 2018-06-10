package marcos.akka.actors

import akka.actor.ActorRef
import akka.actor.ActorSelection
import akka.actor.ActorSystem
import akka.pattern.Patterns
import marcos.akka.configs.SpringExtension
import marcos.akka.configs.toMono
import marcos.akka.extensions.timeout
import marcos.akka.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class LocalService {

    @Autowired
    private lateinit var actorSystem: ActorSystem
    @Autowired
    private lateinit var springExtension: SpringExtension

    fun simpleCallLocalActor() {
        val localActor1: ActorSelection = actorSystem.actorSelection("/user/localActor1")
        localActor1.tell(MessageCommand(MessageType.MESSAGE, "Ola!"), ActorRef.noSender())
    }

    fun callRemoteActor(): Mono<RemoteResponseMessageCommand> {
        val localActor1: ActorSelection = actorSystem.actorSelection("/user/localActor1")
        val message = RemoteRequestMessageCommand(MessageType.MESSAGE, "Ola, remoto!")
        return toMono(Patterns.ask(localActor1, message, timeout))
    }

    fun startAgendamento(): Mono<String> {
        val localActor1: ActorSelection = actorSystem.actorSelection("/user/localActor1")
        val message = TickCommand(time = 10L)
        localActor1.tell(message, ActorRef.noSender())
        return Mono.just("Agendamento iniciado!")
    }

    fun sendHelloToCluster(qtd:Long): Mono<String> {
        val localActor1: ActorSelection = actorSystem.actorSelection("/user/localActor1")
        localActor1.tell(HelloClusterCommand("", qtd), ActorRef.noSender())
        return Mono.just("Hello Sent!")
    }

    fun sendResetToCluster(): Mono<String> {
        val localActor1: ActorSelection = actorSystem.actorSelection("/user/localActor1")
        localActor1.tell(ResetClusterCommand(), ActorRef.noSender())
        return Mono.just("Reset Sent!")
    }
}