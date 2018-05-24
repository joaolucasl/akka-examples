package marcos.akka.actors

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import marcos.akka.configs.SpringExtension
import marcos.akka.model.StartCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ActorCreator {

    @Autowired
    private lateinit var actorSystem: ActorSystem
    @Autowired
    private lateinit var springExtension: SpringExtension

    fun createAllActors() {
        val props: Props = springExtension.get(actorSystem).remoteActorProps("remoteActor", "remoteActor1")
        val remoteActor1: ActorRef = actorSystem.actorOf(props)
        remoteActor1.tell(StartCommand(), ActorRef.noSender())
    }
}
