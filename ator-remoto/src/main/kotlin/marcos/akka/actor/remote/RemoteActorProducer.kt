package marcos.akka.actor.remote

import akka.actor.Actor
import akka.actor.IndirectActorProducer
import org.springframework.context.ApplicationContext

class RemoteActorProducer(private val applicationContext: ApplicationContext, private val beanActorName: String, private val specificName: String) : IndirectActorProducer {
    override fun produce(): Actor {
        return applicationContext.getBean(beanActorName, specificName) as Actor
    }

    override fun actorClass(): Class<out Actor>? {
        return applicationContext.getType(beanActorName) as Class<out Actor>?
    }
}