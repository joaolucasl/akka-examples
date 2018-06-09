package marcos.akka.actors

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import marcos.akka.configs.SpringExtension
import marcos.akka.model.StartCommand
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ActorCreator(var localActorCreated:Boolean=false,
                   var remoteActorCreated:Boolean=false,
                   var agendamentoRemoteActorCreated:Boolean=false,
                   var clusteringActorCreated:Boolean=false,
                   var clusteringSingletonActorCreated:Boolean=false,
                   var clusteringSingletonActorProxyCreated:Boolean=false) {

    @Autowired
    lateinit var actorSystem: ActorSystem
    @Autowired
    private lateinit var springExtension: SpringExtension

    fun createLocalActor():ActorRef {
        val props: Props = springExtension.get(actorSystem).actorProps("localActor", "localActor1")
        return actorSystem.actorOf(props, "localActor1").also {
            it.tell(StartCommand(), ActorRef.noSender())
            localActorCreated = true
        }
    }

    fun createRemoteActor():ActorRef {
        val props: Props = springExtension.get(actorSystem).actorProps("remoteActor", "remoteActor1")
        return  actorSystem.actorOf(props, "remoteActor1").also {
            it.tell(StartCommand(), ActorRef.noSender())
            remoteActorCreated = true
        }
    }

    fun createAgendamentoRemoteActor():ActorRef {
        val props: Props = springExtension.get(actorSystem).actorProps("agendamentoRemoteActor", "agendamentoRemoteActor1")
        return actorSystem.actorOf(props, "agendamentoRemoteActor1").also {
            it.tell(StartCommand(), ActorRef.noSender())
            agendamentoRemoteActorCreated = true
        }
    }

    fun createClusteringActor():ActorRef {
        val props: Props = springExtension.get(actorSystem).actorProps("clusteringActor", "clusteringActor")
        return actorSystem.actorOf(props, "clusteringActor").also {
            clusteringActorCreated = true
        }
    }

    fun createClusteringSingletonEntrypointActor():ActorRef {
        val props: Props = springExtension.get(actorSystem).singletonProps("worker","clusteringSingletonEntrypointActor", "entrypoint")
        return actorSystem.actorOf(props, "singletonManager").also {
            clusteringSingletonActorCreated = true
        }
    }

    fun createClusteringSingletonProxyActor(path:String):ActorRef {
        val props: Props = springExtension.get(actorSystem).singletonProxyProps("worker", path)
        return actorSystem.actorOf(props, "singletonProxy${RandomStringUtils.random(10, "ABCDEFGHIJKLMNOPQRSTUVXZ")}").also {
            clusteringSingletonActorProxyCreated = true
        }
    }
}