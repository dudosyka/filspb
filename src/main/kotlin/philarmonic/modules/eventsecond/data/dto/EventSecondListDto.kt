package philarmonic.modules.eventsecond.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class EventSecondListDto(
    val id:Int,
    val name:String,
    val visible:Boolean,
    val position:Int,
)
