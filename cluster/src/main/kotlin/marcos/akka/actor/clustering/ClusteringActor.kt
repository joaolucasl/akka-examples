package marcos.akka.actor.clustering

import akka.actor.AbstractActor
import akka.cluster.Cluster
import akka.event.Logging
import marcos.akka.extensions.printPretty
import marcos.akka.model.StartCommand
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import akka.cluster.ClusterEvent.UnreachableMember
import akka.cluster.ClusterEvent.MemberEvent
import akka.cluster.ClusterEvent
import akka.cluster.ClusterEvent.MemberRemoved
import akka.cluster.ClusterEvent.MemberUp
import marcos.akka.model.ClusterCommandRequest


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class ClusteringActor(val name: String) : AbstractActor() {
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
                .match(StartCommand::class.java) { printPretty(self.path().toString(), "Started!!")}
                .match(MemberUp::class.java) {
                    mUp -> log.info("Member is Up: {}", mUp.member())
                }
                .match(UnreachableMember::class.java) { mUnreachable -> log.info("Member detected as unreachable: {}", mUnreachable.member()) }
                .match(MemberRemoved::class.java) { mRemoved -> log.info("Member is Removed: {}", mRemoved.member()) }
                //.match(MemberEvent::class.java) { }
                .match(ClusterCommandRequest::class.java) {
                    val currentJVMAddress = getLocalJVMAddress()
                    log.info("Starting to process ${it.messagesToProcess} messages on $currentJVMAddress")

                }

                .matchAny { msg ->
                    unhandled(msg)
                }
                .build()
    }

    private fun getLocalJVMAddress() = context.provider().defaultAddress.toString().split(":")[1].split("@")[1]
}