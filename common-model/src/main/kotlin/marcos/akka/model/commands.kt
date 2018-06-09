package marcos.akka.model

import java.io.Serializable

data class RemoteRequestMessageCommand(val messageType:MessageType, var messageContent:Any="") : Serializable
data class RemoteResponseMessageCommand(val messageType:MessageType, var messageContent:Any="") : Serializable
data class MessageCommand(val messageType:MessageType, var messageContent:Any="") : Serializable

data class StartCommand(val messageType:MessageType=MessageType.MESSAGE) : Serializable
data class HelloCommand(val messageType:MessageType=MessageType.MESSAGE) : Serializable
data class TickCommand(val messageType: MessageType=MessageType.TICK, val time:Long = 5) : Serializable
data class TockCommand(val messageType: MessageType=MessageType.TOCK) : Serializable
data class StopScheduledEventCommand(val messageType: MessageType=MessageType.TOCK) : Serializable

class GetCounterClusterCommand : Serializable
data class ClusterCommandResponse(val selfAddress:String, var currentCount:Long=0) : Serializable
data class IncrementCounterCommand(val value:Long=1) : Serializable

enum class MessageType  : Serializable { MESSAGE,RESPONSE,DONE,TICK,TOCK,STOP_SCHEDULED_EVENT,CLUSTER_GET_COUNT,CLUSTER_INCREMENT_COUNT,CLUSTER_PROCESS_RESPONSE,CLUSTER_INTERNAL_REQUEST,CLUSTER_INTERNAL_RESPONSE}
enum class ClusterMessageType  : Serializable {CLUSTER_INTERNAL_REQUEST,CLUSTER_INTERNAL_RESPONSE,CLUSTER_SINGLETON_END}

data class ClusterSingletonEnd(val messageType: ClusterMessageType=ClusterMessageType.CLUSTER_SINGLETON_END) : Serializable
