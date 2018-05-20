package marcos.akka.model

data class RemoteRequestMessageCommand(val messageType:MessageType, var messageContent:Any="")
data class RemoteResponseMessageCommand(val messageType:MessageType, var messageContent:Any="")
data class MessageCommand(val messageType:MessageType, var messageContent:Any="")

enum class MessageType {
    MESSAGE, RESPONSE, DONE
}