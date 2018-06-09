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
                .match(StartCommand::class.java) { printPretty(self.path().toString(), "Started!!") }
                .match(RemoteRequestMessageCommand::class.java) {
                    "Olá ${context.sender()}, sou o $self, como vai?".apply {
                        sender.tell(RemoteResponseMessageCommand(MessageType.RESPONSE, printPretty(this)), self)
                    }
                }
                .matchAny { unhandled(it) }
                .build()
    }
}