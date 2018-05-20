package marcos.akka.controller

import marcos.akka.application.local.LocalService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AkkaExamplesController {

    @Autowired
    private lateinit var localService: LocalService

    @GetMapping("/local")
    private fun getLocal(){
        localService.simpleCallLocalActor()
    }
}