package marcos.akka.actor.agendamento

import akka.actor.AbstractActor
import marcos.akka.extensions.printPretty
import marcos.akka.model.MessageType
import marcos.akka.model.StartCommand
import marcos.akka.model.TickCommand
import marcos.akka.model.TockCommand
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class AgendamentoRemoteActor(val name: String) : AbstractActor() {

    override fun createReceive(): AbstractActor.Receive {
        return receiveBuilder()
                .match(StartCommand::class.java) { printPretty("$self, Started!!") }
                .match(TickCommand::class.java) {
                    "Olá $sender, recebi um $it.".apply {
                        printPretty(name, this)
                        sender.tell(TockCommand(MessageType.TOCK), self)
                    }
                }
                .matchAny { unhandled(it) }
                .build()
    }
}