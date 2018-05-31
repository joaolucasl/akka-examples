package marcos.akka.actor.remote

import akka.actor.AbstractActor
import marcos.akka.extensions.printPretty
import marcos.akka.model.MessageType
import marcos.akka.model.RemoteRequestMessageCommand
import marcos.akka.model.RemoteResponseMessageCommand
import marcos.akka.model.StartCommand
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class RemoteActor(val name: String) : AbstractActor() {

    override fun createReceive(): AbstractActor.Receive {
        return receiveBuilder()
                .match(StartCommand::class.java, {
                    printPretty(self.path().toString(), "Started!!")
                })
                .match(RemoteRequestMessageCommand::class.java, {
                    printPretty(name, it.messageContent.toString())
                    val msg = "Olá ${context.sender().path().toSerializationFormat().split(":")[1].split("@")[1]}, " +
                            "estou na ${context.provider().defaultAddress.toString().split(":")[1].split("@")[1]}, como vai?"
                    printPretty(name, msg)
                    sender.tell(RemoteResponseMessageCommand(MessageType.RESPONSE, msg), self)
                })
                .matchAny { msg ->
                    unhandled(msg)
                }
                .build()
    }
}