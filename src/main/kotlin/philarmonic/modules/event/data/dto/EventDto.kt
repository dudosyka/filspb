package philarmonic.modules.event.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    val id: Int,
    val hallId: Int?,
    val platformId:Int?,
    val date: Long?,
    val time: String?,
    val purchaseLink: String?,
    val soldOut: Boolean?,
    val position:Int,
    val eventId: Int?,
    val visible:Boolean,
    val price: String?,
)
