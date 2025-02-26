package philarmonic.modules.event.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class UpdateEventDto(
    val platformId:Int?,
    val hallId:Int?,
    val date:Long?,
    val time:String?,
    val purchaseLink:String?,
    val soldOut:Boolean?,
    val position:Int?,
    val eventId:Int?,
    val visible:Boolean?,
    val price:String?,
)
