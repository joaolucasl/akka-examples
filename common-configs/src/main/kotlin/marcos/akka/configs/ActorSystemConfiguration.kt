package marcos.akka.configs

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
        val system = ActorSystem.create("AKKA_EXAMPLES")
        springExtension!!.get(system)
        for(i in 1..10){
            println("-")
        }
        return system
    }
}