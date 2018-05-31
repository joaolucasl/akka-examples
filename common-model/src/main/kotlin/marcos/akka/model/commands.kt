package marcos.akka.model

import java.io.Serializable

data class RemoteRequestMessageCommand(val messageType:MessageType, var messageContent:Any="") : Serializable
data class RemoteResponseMessageCommand(val messageType:MessageType, var messageContent:Any="") : Serializable
data class MessageCommand(val messageType:MessageType, var messageContent:Any="") : Serializable
data class StartCommand(val messageType:MessageType=MessageType.MESSAGE) : Serializable

enum class MessageType  : Serializable {
    MESSAGE, RESPONSE, DONE
}