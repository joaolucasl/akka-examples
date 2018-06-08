package marcos.akka.controller

import marcos.akka.actors.ActorCreator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ClusteringSingletonController @Autowired constructor(private val actorCreator: ActorCreator){
    init {
        actorCreator.createClusteringSingletonEntrypointActor()
    }

    @GetMapping("/ok", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    private fun getOk():String {
        return "{\"ok\":${actorCreator.clusteringSingletonActorCreated}}"
    }
}