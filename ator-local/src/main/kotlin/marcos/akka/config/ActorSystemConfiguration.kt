package marcos.akka.config

import akka.actor.ActorSystem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ActorSystemConfiguration {

    @Autowired
    private val springExtension: SpringExtension? = null

    @Bean
    fun actorSystem(): ActorSystem {
        val system = ActorSystem.create("akka-spring-moip")
        springExtension!!.get(system)
        return system
    }
}