package marcos.akka.application.local

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import marcos.akka.configs.SpringExtension
import marcos.akka.model.MessageCommand
import marcos.akka.model.MessageType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LocalService{

    @Autowired
    private lateinit var actorSystem: ActorSystem
    @Autowired
    private lateinit var springExtension: SpringExtension

    fun simpleCallLocalActor(){
        val props: Props = springExtension.get(actorSystem).localActorProps("localActor", "ator1")
        val ator1: ActorRef = actorSystem.actorOf(props)
        ator1.tell(MessageCommand(MessageType.MESSAGE, "Ola!"), ator1)
    }
}