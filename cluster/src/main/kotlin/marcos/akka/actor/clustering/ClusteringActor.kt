package marcos.akka.actor.clustering

import akka.actor.AbstractActor
import akka.cluster.Cluster
import akka.cluster.ClusterEvent
import akka.cluster.ClusterEvent.*
import akka.cluster.client.ClusterClientReceptionist
import akka.event.Logging
import marcos.akka.extensions.printPretty
import marcos.akka.model.*
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class ClusteringActor(val name: String) : AbstractActor() {

    private var counter: Long = 0

    private val log = Logging.getLogger(context.system, this)
    private val cluster = Cluster.get(context.system)

    override fun preStart() {
        cluster.subscribe(self, ClusterEvent.initialStateAsEvents(),
                MemberEvent::class.java, UnreachableMember::class.java)
        ClusterClientReceptionist.get(context.system).registerService(self)
    }

    override fun postStop() {
        cluster.unsubscribe(self)
    }

    override fun createReceive(): AbstractActor.Receive {
        return receiveBuilder()
                .match(StartCommand::class.java) { printPretty(self.path().toString(), "Started!!") }
                .match(HelloCommand::class.java) { _ ->
                    val message = "${self.path().toSerializationFormat()}: HELLO!"
                    printPretty(message)
                    sender.tell(message, self)
                }
                .match(IncrementCounterCommand::class.java) { counter += it.value }
                .match(GetCounterClusterCommand::class.java, this::handleGetCounterClusterCommand)
                .match(MemberUp::class.java) { printPretty("Member is Up: ${it.member()}") }
                .match(UnreachableMember::class.java) { printPretty("Member detected as unreachable: ${it.member()}") }
                .match(MemberRemoved::class.java) { printPretty("Member is Removed: ${it.member()}") }
                .match(MemberEvent::class.java) { printPretty("${it.member()} received a member event") }
                .matchAny {unhandled(it)}
                .build()
    }

    private fun handleGetCounterClusterCommand(it: GetCounterClusterCommand) {
        sender.tell(ClusterCommandResponse(self.path().toSerializationFormat(), counter), self)
    }

    private fun getLocalJVMAddress() = context.provider().defaultAddress.toString().split(":")[1].split("@")[1]
}