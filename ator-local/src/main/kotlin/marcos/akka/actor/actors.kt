package marcos.akka.actor

import akka.actor.AbstractActor
import marcos.akka.model.Message

class Greeter : AbstractActor() {
    override fun createReceive(): AbstractActor.Receive {
        return receiveBuilder()
                .matchEquals(Message.GREET, {
                    println("Hello World!")
                    sender.tell(Message.DONE, self)
                })
                .matchAny { msg ->
                    unhandled(msg)
                }
                .build()
    }
}