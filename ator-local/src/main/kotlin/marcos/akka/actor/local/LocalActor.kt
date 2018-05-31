package marcos.akka.actor.local

import akka.actor.AbstractActor
import akka.actor.Cancellable
import marcos.akka.extensions.duration
import marcos.akka.extensions.printPretty
import marcos.akka.model.*
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class LocalActor(val name: String) : AbstractActor() {
    private val remoteActor1Path = "akka.tcp://AKKA_EXAMPLES@10.5.0.4:9005/user/remoteActor1"
    private val schedulingRemoteActor1Path = "akka.tcp://AKKA_EXAMPLES@10.5.0.5:9005/user/agendamentoRemoteActor1"
    private var schedulingCancellable:Cancellable? = null

    override fun createReceive(): AbstractActor.Receive {
        return receiveBuilder()
                .match(StartCommand::class.java, {
                    printPretty(self.path().toSerializationFormat(), "Started!!")
                })
                .match(MessageCommand::class.java, {
                    printPretty(name, it.messageContent.toString())
                })
                .match(RemoteRequestMessageCommand::class.java, {
                    val remoteActor1 = context.actorSelection(remoteActor1Path)
                    printPretty(self.path().toSerializationFormat(), "Forwarding to ${remoteActor1.pathString()}")
                    remoteActor1.forward(it, context)
                })
                .match(TickCommand::class.java, {
                    val targetActor = Await.result(context.actorSelection(schedulingRemoteActor1Path).resolveOne(duration), duration)
                    printPretty(name, "Sending TICK scheduling to $schedulingRemoteActor1Path ...")

                    //cancel the last cancellable squeduling
                    schedulingCancellable?.cancel()

                    //create a new schedule
                    schedulingCancellable = context.system.scheduler().schedule(
                            Duration.Zero(),
                            Duration.create(it.time, TimeUnit.SECONDS),
                            targetActor,
                            TickCommand(MessageType.TICK),
                            context.dispatcher(),
                            self)
                })
                .match(TockCommand::class.java, {
                    val localJVM = context.provider().defaultAddress.toString().split(":")[1].split("@")[1]
                    val schedulingJVM = context.sender().path().toSerializationFormat().split(":")[1].split("@")[1]
                    printPretty("$name in $localJVM, received ${it.messageType.name} from $schedulingJVM", it.messageType.name)
                })
                .matchAny { msg ->
                    unhandled(msg)
                }
                .build()
    }
}