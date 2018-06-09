package marcos.akka.configs

import akka.actor.*
import akka.cluster.singleton.ClusterSingletonManager
import akka.cluster.singleton.ClusterSingletonManagerSettings
import akka.cluster.singleton.ClusterSingletonProxy
import akka.cluster.singleton.ClusterSingletonProxySettings
import marcos.akka.actor.clustering.ClusteringSingletonEntrypointActor
import marcos.akka.model.ClusterSingletonEnd
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Service


@Service
class SpringExtension : AbstractExtensionId<SpringExtension.SpringExt>(), ApplicationContextAware {


    @Volatile
    private lateinit var applicationContext: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    override fun createExtension(system: ExtendedActorSystem): SpringExt {
        return SpringExt(applicationContext, system)
    }

    class SpringExt(@field:Volatile private var applicationContext: ApplicationContext?, private val system: ExtendedActorSystem) : Extension {
        private val singletonSettings:ClusterSingletonManagerSettings = ClusterSingletonManagerSettings.create(system)
        private val singletonProxySettings:ClusterSingletonProxySettings = ClusterSingletonProxySettings.create(system)

        fun actorProps(actorBeanName: String, specificName: String): Props {
            return Props.create(
                    ActorProducer::class.java, applicationContext, actorBeanName, specificName)
        }

        fun singletonProps(role: String, actorBeanName: String, specificName: String): Props {
            return ClusterSingletonManager.props(
                    actorProps(actorBeanName, specificName),
                    PoisonPill.getInstance(), singletonSettings)
        }

        fun singletonProxyProps(role: String, path:String): Props {
            return ClusterSingletonProxy.props(path,
                    singletonProxySettings)
        }
    }
}