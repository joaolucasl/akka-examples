package marcos.akka.actors

import akka.actor.ActorRef
import akka.actor.ActorSelection
import akka.actor.ActorSystem
import akka.pattern.Patterns.ask
import marcos.akka.configs.SpringExtension
import marcos.akka.configs.toMono
import marcos.akka.extensions.timeout
import marcos.akka.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class LocalService @Autowired constructor(private val actorSystem: ActorSystem, private val springExtension: SpringExtension) {

    fun simpleCallLocalActor():Mono<String> {
        val localActor1: ActorSelection = actorSystem.actorSelection("/user/localActor1")
        localActor1.tell(MessageCommand(MessageType.MESSAGE, "Ola!"), ActorRef.noSender())
        return Mono.just("Chamei o localActor1!")
    }

    fun callRemoteActor(): Mono<RemoteResponseMessageCommand> {
        val localActor1: ActorSelection = actorSystem.actorSelection("/user/localActor1")
        val message = RemoteRequestMessageCommand(MessageType.MESSAGE, "Ola, remoto!")
        //this is the 'sender' which is forwarded to Actor(/user/remoteActor1)
        return toMono(ask(localActor1, message, timeout))
    }

    fun startAgendamento(): Mono<String> {
        val localActor1: ActorSelection = actorSystem.actorSelection("/user/localActor1")
        val message = TickStartCommand(time = 3)
        localActor1.tell(message, ActorRef.noSender())
        return Mono.just("Agendamento iniciado!")
    }

    fun stopAgendamento(): Mono<String> {
        val localActor1: ActorSelection = actorSystem.actorSelection("/user/localActor1")
        val message = StopScheduledEventCommand()
        localActor1.tell(message, ActorRef.noSender())
        return Mono.just("Mandei o agendamento parar!")
    }

    fun sendIncrementToCluster(qtd:Long): Mono<String> {
        val localActor1: ActorSelection = actorSystem.actorSelection("/user/localActor1")
        localActor1.tell(IncrementCounterCommand("", qtd), ActorRef.noSender())
        return Mono.just("Enviei $qtd mensagem(ns) pro cluster!")
    }

    fun sendResetToCluster(): Mono<String> {
        val localActor1: ActorSelection = actorSystem.actorSelection("/user/localActor1")
        localActor1.tell(ResetClusterCommand(), ActorRef.noSender())
        return Mono.just("Enviei um reset pro cluster!")
    }
}