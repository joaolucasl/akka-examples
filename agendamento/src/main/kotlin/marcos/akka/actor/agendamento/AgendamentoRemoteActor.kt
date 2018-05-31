package marcos.akka.actor.agendamento

import akka.actor.AbstractActor
import marcos.akka.extensions.printPretty
import marcos.akka.model.*
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class AgendamentoRemoteActor(val name: String) : AbstractActor() {

    override fun createReceive(): AbstractActor.Receive {
        return receiveBuilder()
                .match(StartCommand::class.java, {
                    printPretty(self.path().toString(), "Started!!")
                })
                .match(TickCommand::class.java, {
                    val msg = "Olá ${context.sender().path().toSerializationFormat().split(":")[1].split("@")[1]}, \n" +
                            "recebi um ${it.messageType.name}. \n" +
                            "Estou na ${context.provider().defaultAddress.toString().split(":")[1].split("@")[1]}"
                    printPretty(name, msg)
                    sender.tell(TockCommand(MessageType.TOCK), self)
                })
                .matchAny {
                    unhandled(it)
                }
                .build()
    }
}