package marcos.akka.model

import java.io.Serializable

data class RemoteRequestMessageCommand(val messageType:MessageType, var messageContent:Any="") : Serializable
data class RemoteResponseMessageCommand(val messageType:MessageType, var messageContent:Any="") : Serializable
data class MessageCommand(val messageType:MessageType, var messageContent:Any="") : Serializable
data class StartCommand(val messageType:MessageType=MessageType.MESSAGE) : Serializable
data class TickCommand(val messageType: MessageType=MessageType.TICK, val time:Long = 5) : Serializable
data class TockCommand(val messageType: MessageType=MessageType.TOCK) : Serializable
enum class MessageType  : Serializable { MESSAGE, RESPONSE, DONE, TICK, TOCK }