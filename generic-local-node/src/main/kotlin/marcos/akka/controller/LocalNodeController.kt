package marcos.akka.controller

import marcos.akka.actors.ActorCreator
import marcos.akka.actors.LocalService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class LocalNodeController @Autowired constructor(private val actorCreator: ActorCreator, private val localService: LocalService) {
    init {
        actorCreator.createLocalActor()
    }

    @GetMapping("/ok", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    private fun getOk(): String {
        return "{\"ok\":${actorCreator.localActorCreated}}"
    }



    @GetMapping("/local")
    private fun getLocal() = localService.simpleCallLocalActor()

    @GetMapping("/remote")
    private fun getRemote(): Mono<String> = localService.callRemoteActor().map { it.messageContent as String }



    @GetMapping("/remote/agendamento/start")
    private fun agendamentoStart(): Mono<String> = localService.startAgendamento()

    @GetMapping("/remote/agendamento/stop")
    private fun agendamentoStop(): Mono<String> = localService.stopAgendamento()



    @GetMapping("/cluster/increment")
    private fun sendIncrementToCluster(@RequestParam qtd:Long?=0L): Mono<String> = localService.sendIncrementToCluster(qtd?:0L)

    @GetMapping("/cluster/reset")
    private fun sendResetToCluster(): Mono<String> = localService.sendResetToCluster()
}