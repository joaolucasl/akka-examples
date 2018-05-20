package marcos.akka.extensions

import akka.actor.ActorRef
import akka.actor.ActorRefFactory
import akka.actor.Props
import akka.actor.UntypedActor
import akka.util.Timeout
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass


val duration = FiniteDuration.create(10, TimeUnit.SECONDS)
val timeout = Timeout.durationToTimeout(duration)

fun printPretty(ref:String, msg:String){
    println("")
    println(">>>>>>>>>>>>>>>>  FROM $ref: message: \"$msg\"")
    println("")
}
fun akkaMain(vararg clazzes: KClass<*>) {
    val array = clazzes.map { it.java.name }.toTypedArray()
    akka.Main.main(array)
}

inline fun <reified T : Any> createProps(): Props {
    return Props.create(T::class.java)
}

inline fun <reified T : Any> createProps(actor: ActorRef): Props {
    return Props.create(T::class.java, actor)
}


inline fun <reified T : Any> ActorRefFactory.actorOf(name: String): ActorRef {
    return actorOf(createProps<T>(), name)
}

inline fun <reified T : Any> UntypedActor.actorOf(name: String): ActorRef {
    return context.actorOf(Props.create(T::class.java), name)
}

fun UntypedActor.tell(actor: ActorRef, msg: Any?) {
    actor.tell(msg, this.self)
}