package marcos.akka.config

import akka.actor.AbstractExtensionId
import akka.actor.ExtendedActorSystem
import akka.actor.Extension
import akka.actor.Props
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class SpringExtension : AbstractExtensionId<SpringExtension.SpringExt>(), ApplicationContextAware {

    @Volatile
    private lateinit var applicationContext: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    override fun createExtension(system: ExtendedActorSystem): SpringExt {
        return SpringExt(applicationContext)
    }

    class SpringExt(@field:Volatile private var applicationContext: ApplicationContext?) : Extension {

        fun props(actorBeanName: String, accountId: String): Props {
            return Props.create(
                    AtorLocalProducer::class.java, applicationContext, actorBeanName, accountId)
        }
    }
}