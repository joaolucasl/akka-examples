package marcos.akka.configs

import reactor.core.publisher.Mono
import scala.compat.java8.FutureConverters
import scala.concurrent.Future

fun <T> toMono(future: Future<Any>): Mono<T> {
    return Mono.fromFuture(
            FutureConverters.toJava(future).toCompletableFuture().thenApplyAsync { obj -> obj as T })
}