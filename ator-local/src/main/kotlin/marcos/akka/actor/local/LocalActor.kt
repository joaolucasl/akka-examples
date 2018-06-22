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

    //ref to the last scheduling applied
    private var schedulingCancellable: Cancellable? = null

    //ref to cluster Receptionist
    private var clusterReceptionist: ActorRef? = null

    override fun preStart() {
        //create an actorClient as a ref to cluster Receptionist
        clusterReceptionist = when (nodeType) {
            NodeType.RECEPTIONIST ->
                context.system.actorOf(
                        ClusterClient.props(
                                ClusterClientSettings.create(context.system)
                                        .withInitialContacts(initialContacts())), "clusterReceptionist")
            else -> null
        }
    }

    override fun createReceive(): AbstractActor.Receive {
        return receiveBuilder()
                //print start message
                .match(StartCommand::class.java) { printPretty("${self.path()} Started!!") }

                //print received generic message
                .match(MessageCommand::class.java) {
                    printPretty("ts: ${System.currentTimeMillis() / 1000} ${self.path()} received ${it.messageContent}")
                }

                //sending RemoteRequestMessage command to remote actor
                .match(RemoteRequestMessageCommand::class.java, this::handleRemoteRequestMessageCommand)

                //starting TICK command schedule..
                .match(TickStartCommand::class.java, this::handleTickStartCommand)

                //receiving TOCK command from remote schedulingActor
                .match(TockCommand::class.java) {
                    printPretty("${self.path()} received $it from ${context.sender().path()}")
                }

                //stopping scheduled event
                .match(StopScheduledEventCommand::class.java) { _ ->
                    schedulingCancellable?.cancel().also { _ ->
                        printPretty("agendamento paralizado e removido!")
                    }
                }

                //tell receptionist to propagate the message
                .match(IncrementCounterCommand::class.java) {
                    it.copy(selfAddress = self.toString()).apply {
                        sendClusterEvent(this.qtd, this).also {
                            printPretty("Estou enviando um zilhão (ps.:${this.qtd}) de commandos para o Cluster!")
                        }
                    }
                }

                //tell receptionist to propagate the message
                .match(ResetClusterCommand::class.java) {
                    sendClusterEvent(0, it).also {
                        printPretty("I'm sending a ResetCommand to Cluster!")
                    }
                }

                //unhandled
                .matchAny { unhandled(it) }
                .build()
    }

    private fun sendClusterEvent(qtd: Long, msgToSend: Any) {
        if (qtd == 0L) { //tell all cluster actors to reset counter
            clusterReceptionist?.tell(ClusterClient.SendToAll("/user/clusteringActor", msgToSend), self)

        } else { //send a bunch of messages to a random actor on the same path!
            for (i: Long in 1..qtd) {
                clusterReceptionist?.tell(ClusterClient.Send("/user/clusteringActor", msgToSend), self)
            }
        }
    }

    private fun handleTickStartCommand(it: TickStartCommand) {
        //get a ref to schedulingRemoteActor
        val targetRemoteActor = Await.result(context.actorSelection(schedulingRemoteActor1Path).resolveOne(duration), duration)
        printPretty(name, "Sending TICK scheduling to $schedulingRemoteActor1Path ...")

        //cancel the last cancellable squeduling
        schedulingCancellable?.cancel()

        //create a new schedule
        schedulingCancellable = context.system.scheduler().schedule(
                Duration.Zero(), //init after $duration...
                Duration.create(it.time, TimeUnit.SECONDS), //repeat after $duration...
                targetRemoteActor,
                TickCommand(),
                context.dispatcher(),
                self)
    }

    private fun handleRemoteRequestMessageCommand(it: RemoteRequestMessageCommand) {
        //get a ref to remoteActor
        val remoteActor1 = context.actorSelection(remoteActor1Path)
        //print some logs..
        printPretty(self.path().toSerializationFormat(), "Forwarding to ${remoteActor1.pathString()}")
        //forward message to remoteActor
        remoteActor1.forward(it, context) //context contains 'sender' which is forwarded to Actor(/user/remoteActor1)
    }

    companion object {
        private const val remoteActor1Path = "akka.tcp://AKKA_EXAMPLES@10.5.0.4:9005/user/remoteActor1"
        private const val schedulingRemoteActor1Path = "akka.tcp://AKKA_EXAMPLES@10.5.0.5:9005/user/agendamentoRemoteActor1"

        var nodeType = NodeType.LOCAL

        //return the inital contacts for the Receptionist
        private fun initialContacts(): HashSet<ActorPath> = HashSet(
                Arrays.asList(
                        ActorPaths.fromString("akka.tcp://AKKA_EXAMPLES@10.5.0.13:9005/system/recepcionista"),
                        ActorPaths.fromString("akka.tcp://AKKA_EXAMPLES@10.5.0.14:9005/system/recepcionista")))
    }
}