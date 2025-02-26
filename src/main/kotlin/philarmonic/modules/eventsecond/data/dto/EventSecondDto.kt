package philarmonic.modules.eventsecond.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class EventSecondDto(
    val id: Int,
    val name:String,
    val image:String? = null,
    val shortDescription: String?,
    val duration: String?,
    val description: String?,
    val authors: String,
    val tags: List<Int>,
    val visible:Boolean,
    val position:Int,
)
