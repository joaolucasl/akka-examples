package marcos.akka.controller

import marcos.akka.application.general.GeneralService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AkkaExamplesController {

    @Autowired
    private lateinit var generalService: GeneralService

    @GetMapping("/local")
    private fun getLocal() {
        generalService.simpleCallLocalActor()
    }

    @GetMapping("/remote")
    private fun getRemote() {
        generalService.callRemoteActor()
    }
}