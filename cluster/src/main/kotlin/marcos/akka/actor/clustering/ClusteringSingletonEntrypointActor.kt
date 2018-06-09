package marcos.akka.actor.clustering

import akka.actor.AbstractActor
import akka.actor.ActorRef
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
import akka.routing.RoundRobinGroup
import akka.routing.RoundRobinPool
import marcos.akka.model.ClusterCommandIncrementCounter
import java.util.*


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class ClusteringSingletonEntrypointActor(val name: String) : AbstractActor() {
    val log = Logging.getLogger(context.system, this)
    val cluster = Cluster.get(context.system)

    val router: ActorRef = createRouter()

    override fun preStart() {
        cluster.subscribe(self, ClusterEvent.initialStateAsEvents(),
                MemberEvent::class.java, UnreachableMember::class.java)
    }

    override fun postStop() {
        cluster.unsubscribe(self)
    }

    private fun createRouter():ActorRef{
        val paths = Arrays.asList("/user/clusteringActor")
        //https://github.com/kamkor/akka-cluster-load-balancing/tree/master/src/main
        return context.actorOf(RoundRobinPool(5).props(), "clusterActors")
    }

    override fun createReceive(): AbstractActor.Receive {
        return receiveBuilder()
                .match(StartCommand::class.java) { log.info(">>>> {} {}", self.path().toString(), "Started!!") }
                .match(MemberUp::class.java) { log.info(">>>> Member is Up: {}", it.member()) }
                .match(UnreachableMember::class.java) { log.info(">>>> Member detected as unreachable: {}", it.member()) }
                .match(MemberRemoved::class.java) { log.info(">>>> Member is Removed: {}", it.member()) }
                .match(MemberEvent::class.java) { /* ignoring */ }

                .match(ClusterCommandIncrementCounter::class.java, this::handleClusterCommand)
                .match(ClusterCommandRequest::class.java, this::handleClusterCommand)
                .match(ClusterSingletonEnd::class.java) { self.tell(PoisonPill.getInstance(), self) }

                .matchAny { unhandled(it) }
                .build()
    }

    private fun handleClusterCommand(it: Any) {
        log.info(">>>> Forwaring $it to ${router.path().toSerializationFormat()}..")
        router.forward(it, context)
    }

    private fun getLocalJVMAddress() = context.provider().defaultAddress.toString().split(":")[1].split("@")[1]
}