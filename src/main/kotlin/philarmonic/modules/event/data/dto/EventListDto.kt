package philarmonic.modules.event.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class EventListDto(
    val id:Int,
    val date: Long,
    val time: String,
    val EventSecondName:String,
    val PlatformName:String,
    val HallName:String,
    val visible:Boolean,
    val position:Int,
)
