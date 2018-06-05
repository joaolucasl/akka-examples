package marcos.akka.actors

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import marcos.akka.configs.SpringExtension
import marcos.akka.model.StartCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ActorCreator(var localActorCreated:Boolean=false, var remoteActorCreated:Boolean=false, var agendamentoRemoteActorCreated:Boolean=false, var clusteringActorCreated:Boolean=false) {

    @Autowired
    private lateinit var actorSystem: ActorSystem
    @Autowired
    private lateinit var springExtension: SpringExtension

    fun createLocalActor() {
        val props: Props = springExtension.get(actorSystem).actorProps("localActor", "localActor1")
        val localActor1: ActorRef = actorSystem.actorOf(props, "localActor1")
        localActor1.tell(StartCommand(), ActorRef.noSender())
        localActorCreated = true
    }

    fun createRemoteActor() {
        val props: Props = springExtension.get(actorSystem).actorProps("remoteActor", "remoteActor1")
        val remoteActor1: ActorRef = actorSystem.actorOf(props, "remoteActor1")
        remoteActor1.tell(StartCommand(), ActorRef.noSender())
        remoteActorCreated = true
    }

    fun createAgendamentoRemoteActor() {
        val props: Props = springExtension.get(actorSystem).actorProps("agendamentoRemoteActor", "agendamentoRemoteActor1")
        val agendamentoRemoteActor1: ActorRef = actorSystem.actorOf(props, "agendamentoRemoteActor1")
        agendamentoRemoteActor1.tell(StartCommand(), ActorRef.noSender())
        agendamentoRemoteActorCreated = true
    }

    fun createClusteringActor() {
        val props: Props = springExtension.get(actorSystem).actorProps("clusteringActor", "clusteringActor")
        val clusteringActor: ActorRef = actorSystem.actorOf(props, "clusteringActor")
        clusteringActor.tell(StartCommand(), ActorRef.noSender())
        clusteringActorCreated = true
    }

    fun createClusteringSingletonEntrypointActor() {
        val props: Props = springExtension.get(actorSystem).actorProps("clusteringActor", "clusteringActor")
        val clusteringActor: ActorRef = actorSystem.actorOf(props, "clusteringActor")
        clusteringActor.tell(StartCommand(), ActorRef.noSender())
        clusteringActorCreated = true
    }
}