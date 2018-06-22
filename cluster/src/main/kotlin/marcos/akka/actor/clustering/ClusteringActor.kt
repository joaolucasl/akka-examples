package marcos.akka.actor.clustering

import akka.actor.AbstractActor
import akka.cluster.Cluster
import akka.cluster.ClusterEvent
import akka.cluster.ClusterEvent.*
import akka.cluster.client.ClusterClientReceptionist
import marcos.akka.extensions.printPretty
import marcos.akka.model.IncrementCounterCommand
import marcos.akka.model.ResetClusterCommand
import marcos.akka.model.StartCommand
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class ClusteringActor(val name: String) : AbstractActor() {

    private var counter: Long = 0
    private val cluster = Cluster.get(context.system)

    override fun preStart() {
        //subscribe to cluster's Member Events
        cluster.subscribe(self, ClusterEvent.initialStateAsEvents(),
                MemberEvent::class.java, UnreachableMember::class.java)
        //register himself to be available to Receptionist
        ClusterClientReceptionist.get(context.system).registerService(self)
    }

    override fun postStop() {
        //unsubscribe from cluster
        cluster.unsubscribe(self)
        //unregister from Receptionist
        ClusterClientReceptionist.get(context.system).unregisterService(self)
    }

    override fun createReceive(): AbstractActor.Receive {
        return receiveBuilder()
                //===================================================================================================
                // Member events...
                //===================================================================================================
                .match(MemberUp::class.java) { printPretty("Member is Up: ${it.member()}") }
                .match(UnreachableMember::class.java) { printPretty("Member detected as unreachable: ${it.member()}") }
                .match(MemberRemoved::class.java) { printPretty("Member is Removed: ${it.member()}") }
                .match(MemberEvent::class.java) { /* ignored */ }
                //===================================================================================================

                //print start message
                .match(StartCommand::class.java) {
                    printPretty(self.path().toString(), "Started with $it")
                }

                //increment and print Counter message
                .match(IncrementCounterCommand::class.java) {
                    printPretty(incrementAndGenerateCounterMessage(it))
                }

                //reset and print current counter
                .match(ResetClusterCommand::class.java) { _ ->
                    counter = 0L
                    printPretty(generateResetCounterMessage())
                }
                //unhandled
                .matchAny { unhandled(it) }
                .build()
    }

    private fun incrementAndGenerateCounterMessage(cmd:IncrementCounterCommand) =
            "ts: ${System.currentTimeMillis() / 1000}, counter: ${++counter}, $self: hey! --${cmd.selfAddress}!"

    private fun generateResetCounterMessage() =
            "ts: ${System.currentTimeMillis() / 1000}, counter: $counter, $self: Resetting counter to 0"

}