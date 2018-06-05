package marcos.akka.model

import java.io.Serializable

data class RemoteRequestMessageCommand(val messageType:MessageType, var messageContent:Any="") : Serializable
data class RemoteResponseMessageCommand(val messageType:MessageType, var messageContent:Any="") : Serializable
data class MessageCommand(val messageType:MessageType, var messageContent:Any="") : Serializable

data class StartCommand(val messageType:MessageType=MessageType.MESSAGE) : Serializable
data class TickCommand(val messageType: MessageType=MessageType.TICK, val time:Long = 5) : Serializable
data class TockCommand(val messageType: MessageType=MessageType.TOCK) : Serializable
data class StopScheduledEventCommand(val messageType: MessageType=MessageType.TOCK) : Serializable

data class ClusterCommandRequest(var messagesToProcess:Long=0,val messageType: MessageType=MessageType.CLUSTER_PROCESS_REQUEST) : Serializable
data class ClusterCommandResponse(var processedQtd:Long=0,val messageType: MessageType=MessageType.CLUSTER_PROCESS_RESPONSE) : Serializable
data class ClusterInternalCommand(val messageType: ClusterMessageType, var messageContent:Any) : Serializable
enum class MessageType  : Serializable { MESSAGE,RESPONSE,DONE,TICK,TOCK,STOP_SCHEDULED_EVENT,CLUSTER_PROCESS_REQUEST,CLUSTER_PROCESS_RESPONSE,CLUSTER_INTERNAL_REQUEST,CLUSTER_INTERNAL_RESPONSE}
enum class ClusterMessageType  : Serializable {CLUSTER_INTERNAL_REQUEST,CLUSTER_INTERNAL_RESPONSE,CLUSTER_SINGLETON_END}

data class ClusterSingletonEnd(val messageType: ClusterMessageType=ClusterMessageType.CLUSTER_SINGLETON_END) : Serializable
