package marcos.akka.configs

import akka.actor.AbstractExtensionId
import akka.actor.ExtendedActorSystem
import akka.actor.Extension
import akka.actor.Props
import marcos.akka.actor.local.LocalActorProducer
import marcos.akka.actor.remote.RemoteActorProducer
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Service

@Service
class SpringExtension : AbstractExtensionId<SpringExtension.SpringExt>(), ApplicationContextAware {


    @Volatile
    private lateinit var applicationContext: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
        val a1: LocalActorProducer = LocalActorProducer(applicationContext, "", "")
        val a2: RemoteActorProducer = RemoteActorProducer(applicationContext, "", "")
    }

    override fun createExtension(system: ExtendedActorSystem): SpringExt {
        return SpringExt(applicationContext)
    }

    class SpringExt(@field:Volatile private var applicationContext: ApplicationContext?) : Extension {

        fun localActorProps(actorBeanName: String, specificName: String): Props {
            return Props.create(
                    LocalActorProducer::class.java, applicationContext, actorBeanName, specificName)
        }

        fun remoteActorProps(actorBeanName: String, specificName: String): Props {
            return Props.create(
                    RemoteActorProducer::class.java, applicationContext, actorBeanName, specificName)
        }
    }
}