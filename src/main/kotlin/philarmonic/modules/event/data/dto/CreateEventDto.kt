package philarmonic.modules.event.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class CreateEventDto(
    val hallId:Int? = null,
    val platformId:Int? = null,
    val date:Long? = null,
    val time:String? = null,
    val purchaseLink:String? = null,
    val soldOut:Boolean? = null,
    val position:Int,
    val eventId:Int? = null,
    val visible:Boolean,
    val price:String? = null,
)
