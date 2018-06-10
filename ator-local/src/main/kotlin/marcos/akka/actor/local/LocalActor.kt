package marcos.akka.actor.local

import akka.actor.*
import akka.cluster.client.ClusterClient
import akka.cluster.client.ClusterClientSettings
import marcos.akka.extensions.duration
import marcos.akka.extensions.printPretty
import marcos.akka.model.*
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.util.*
import java.util.concurrent.TimeUnit


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class LocalActor(val name: String) : AbstractActor() {
    private val remoteActor1Path = "akka.tcp://AKKA_EXAMPLES@10.5.0.4:9005/user/remoteActor1"
    private val schedulingRemoteActor1Path = "akka.tcp://AKKA_EXAMPLES@10.5.0.5:9005/user/agendamentoRemoteActor1"
    private var schedulingCancellable: Cancellable? = null

    private var clusterClient: ActorRef? = null

    override fun preStart() {
        clusterClient = context.system.actorOf(
                ClusterClient.props(
                        ClusterClientSettings.create(context.system)
                                .withInitialContacts(initialContacts())), "clusterClient")
    }

    override fun createReceive(): AbstractActor.Receive {
        return receiveBuilder()
                //print start message
                .match(StartCommand::class.java) { printPretty("${self.path()} Started!!") }
                //print received generic message
                .match(MessageCommand::class.java) { printPretty("${self.path()} received ${it.messageContent}") }

                //sending RemoteRequestMessage command to remote actor
                .match(RemoteRequestMessageCommand::class.java, this::handleRemoteRequestMessageCommand)
                //sending TICK command to remote schedulingActor
                .match(TickCommand::class.java, this::handleTickCommand)
                //sending TOCK command to remote schedulingActor
                .match(TockCommand::class.java) { printPretty("${self.path()} received ${it.messageType.name} from ${context.sender().path()}") }
                //stopping scheduled event
                .match(StopScheduledEventCommand::class.java) { schedulingCancellable?.cancel() }

                //sending to receptionist propagated message
                .match(HelloClusterCommand::class.java) {it.copy(selfAddress = self.toString()).apply {sendClusterEvent(this.qtd, this)}}
                //sending to receptionist propagated message
                .match(ResetClusterCommand::class.java) { sendClusterEvent(0, it) }

                //unhandled
                .matchAny { unhandled(it) }
                .build()
    }

    private fun sendClusterEvent(qtd: Long, msgToSend: Any) {
        if (qtd == 0L) {
            clusterClient?.tell(ClusterClient.SendToAll("/user/clusteringActor", msgToSend), self)
        } else {
            for (i:Long in 1..qtd) {
                clusterClient?.tell(ClusterClient.Send("/user/clusteringActor", msgToSend), self)
            }
        }
    }

    private fun handleTickCommand(it: TickCommand) {
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
    }

    private fun handleRemoteRequestMessageCommand(it: RemoteRequestMessageCommand) {
        val remoteActor1 = context.actorSelection(remoteActor1Path)
        printPretty(self.path().toSerializationFormat(), "Forwarding to ${remoteActor1.pathString()}")
        remoteActor1.forward(it, context)
    }

    companion object {
        fun initialContacts(): Set<ActorPath> {
            return HashSet<ActorPath>(Arrays.asList(
                    ActorPaths.fromString("akka.tcp://AKKA_EXAMPLES@10.5.0.13:9005/system/recepcionista"),
                    ActorPaths.fromString("akka.tcp://AKKA_EXAMPLES@10.5.0.14:9005/system/recepcionista")))
        }
    }
}