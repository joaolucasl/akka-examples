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
                //print start message
                .match(StartCommand::class.java) { _ ->
                    printPretty(self.path().toString(), "Started!!")
                }
                //answer 'sender' actor
                .match(RemoteRequestMessageCommand::class.java) { _ ->
                    "Olá ${context.sender()}, sou o $self, como vai?".answer(this)
                }
                //unhandled
                .matchAny { unhandled(it) }
                .build()
    }

}

//answer 'sender' actor
private fun String.answer(myself: RemoteActor) {
    myself.sender.tell(RemoteResponseMessageCommand(MessageType.RESPONSE, printPretty(this)), myself.self)
}
