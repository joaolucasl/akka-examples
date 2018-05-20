package marcos.akka.actor.remote

import akka.actor.AbstractActor
import marcos.akka.extensions.printPretty
import marcos.akka.model.MessageType
import marcos.akka.model.RemoteRequestMessageCommand
import marcos.akka.model.RemoteResponseMessageCommand
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class RemoteActor(val name: String) : AbstractActor() {

    override fun createReceive(): AbstractActor.Receive {
        return receiveBuilder()
                .match(RemoteRequestMessageCommand::class.java, {
                    printPretty(name, it.messageContent.toString())
                    sender.tell(RemoteResponseMessageCommand(MessageType.RESPONSE, "Olá ${context.sender().path().name()}!"), self)
                })
                .matchAny { msg ->
                    unhandled(msg)
                }
                .build()
    }
}