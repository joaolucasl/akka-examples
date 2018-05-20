package marcos.akka.actor.local

import akka.actor.AbstractActor
import marcos.akka.extensions.printPretty
import marcos.akka.model.MessageCommand
import marcos.akka.model.RemoteRequestMessageCommand
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class LocalActor(val name: String) : AbstractActor() {

    override fun createReceive(): AbstractActor.Receive {
        return receiveBuilder()
                .match(MessageCommand::class.java, {
                    printPretty(name, it.messageContent.toString())
                    //sender.tell(MessageType.DONE, self)
                })
                .match(RemoteRequestMessageCommand::class.java, {
                    val atorRemoto1 = context.actorSelection("akka.tcp://AKKA_EXAMPLES@remoteJMV1:9005/user/atorRemoto1")
                    atorRemoto1.forward(it, context)
                })
                .matchAny { msg ->
                    unhandled(msg)
                }
                .build()
    }
}