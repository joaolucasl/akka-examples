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
                //print start message
                .match(StartCommand::class.java) { _ ->
                    printPretty("$self, Started!!")
                }

                //print and answer 'sender' with TOCK command!
                .match(TickCommand::class.java) {
                    "Olá $sender, recebi um $it.".printAndSend(this)
                }
                //unhandled
                .matchAny { unhandled(it) }
                .build()
    }
}

private fun String.printAndSend(myself:AgendamentoRemoteActor) {
    //print on sysOut
    printPretty(myself.name, this)
    //responde com TOCK!
    myself.sender.tell(TockCommand(MessageType.TOCK), myself.self)
}
