package philarmonic.modules.seasonevent.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class SeasonEventListDto(
    val id: Int,
    val name: String,
    val isActive: Boolean,
    val visible:Boolean,
    val position:Int,
)
