package philarmonic.modules.eventsecond.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class UpdateEventSecondDto(
    val name:String?,
    val image:String?,
    val imageName:String?,
    val shortDescription:String?,
    val duration:String?,
    val description:String?,
    val authors: String?,
    val tags: List<Int>?,
    val visible:Boolean?,
    val position:Int?,
)
