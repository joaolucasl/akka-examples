package marcos.akka.actor.clustering

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.PoisonPill
import akka.cluster.ClusterEvent.*
import akka.event.Logging
import akka.routing.FromConfig
import marcos.akka.extensions.printPretty
import marcos.akka.model.*
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class ClusteringSingletonEntrypointActor(val name: String) : AbstractActor() {
    private val log = Logging.getLogger(context.system, this)
    //private val cluster = Cluster.get(context.system)
    private var router: ActorRef? = null

    private fun createRouter(): ActorRef {
        //https://github.com/kamkor/akka-cluster-load-balancing/tree/master/src/main
        return context.actorOf(FromConfig.getInstance().props()).also {
            printPretty("My ROUTER: ${self.path()}")
        }
    }

    override fun preStart() {
        log.info("starting: ${self.path()}")
        //cluster.subscribe(self, ClusterEvent.initialStateAsEvents(),
        //        MemberEvent::class.java, UnreachableMember::class.java)
    }

    override fun postStop() {
        //cluster.unsubscribe(self)
    }

    override fun createReceive(): AbstractActor.Receive {
        return receiveBuilder()
                .match(StartCommand::class.java) { printPretty("${self.path()} Started!!") }
                .match(MemberUp::class.java) { printPretty("Member is Up: ${it.member()}") }
                .match(UnreachableMember::class.java) { printPretty("Member detected as unreachable: ${it.member()}") }
                .match(MemberRemoved::class.java) { printPretty("Member is Removed: ${it.member()}") }
                .match(MemberEvent::class.java) { printPretty("${it.member()} received a member event") }
                .match(HelloCommand::class.java) { _ -> "${self.path()}: HELLO!".also { printPretty(it); sender.tell(it, self) } }
                .match(IncrementCounterCommand::class.java, this::handleClusterCommand)
                .match(GetCounterClusterCommand::class.java, this::handleClusterCommand)
                .match(ClusterSingletonEnd::class.java) { self.tell(PoisonPill.getInstance(), self) }
                .matchAny { unhandled(it) }
                .build()
    }

    private fun handleClusterCommand(it: Any) {
        router = router ?: createRouter()
        printPretty("Forwaring $it to ${router?.path()}..")
        router?.forward(it, context)
    }

    private fun getLocalJVMAddress() = context.provider().defaultAddress.toString().split(":")[1].split("@")[1]

    companion object {
        const val actorName: String = "entrypoint"
    }
}