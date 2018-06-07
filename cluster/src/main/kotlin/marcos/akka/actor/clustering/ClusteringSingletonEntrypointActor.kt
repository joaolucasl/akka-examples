package marcos.akka.actor.clustering

import akka.actor.AbstractActor
import akka.actor.PoisonPill
import akka.cluster.Cluster
import akka.cluster.ClusterEvent
import akka.cluster.ClusterEvent.*
import akka.event.Logging
import marcos.akka.extensions.printPretty
import marcos.akka.model.ClusterCommandRequest
import marcos.akka.model.ClusterSingletonEnd
import marcos.akka.model.StartCommand
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class ClusteringSingletonEntrypointActor(val name: String) : AbstractActor() {
    val log = Logging.getLogger(context.system, this)
    val cluster = Cluster.get(context.system)

    override fun preStart() {
        cluster.subscribe(self, ClusterEvent.initialStateAsEvents(),
                MemberEvent::class.java, UnreachableMember::class.java)
    }

    override fun postStop() {
        cluster.unsubscribe(self)
    }

    override fun createReceive(): AbstractActor.Receive {
        return receiveBuilder()
                .match(MemberUp::class.java) { log.info("Member is Up: {}", it.member()) }
                .match(UnreachableMember::class.java) { log.info("Member detected as unreachable: {}", it.member()) }
                .match(MemberRemoved::class.java) { log.info("Member is Removed: {}", it.member()) }
                .match(MemberEvent::class.java) { /* ignoring */ }
                .match(StartCommand::class.java) { printPretty(self.path().toString(), "Started!!") }
                .match(ClusterCommandRequest::class.java, this::handleClusterCommandRequest)
                .match(ClusterSingletonEnd::class.java) { self.tell(PoisonPill.getInstance(), self) }
                .matchAny { unhandled(it) }
                .build()
    }

    private fun handleClusterCommandRequest(it: ClusterCommandRequest){
        log.info("Starting to process ${it.messagesToProcess} messages on ${getLocalJVMAddress()}")
        
    }

    private fun getLocalJVMAddress() = context.provider().defaultAddress.toString().split(":")[1].split("@")[1]
}