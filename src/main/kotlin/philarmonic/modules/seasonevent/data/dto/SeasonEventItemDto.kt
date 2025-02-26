package philarmonic.modules.seasonevent.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class SeasonEventItemDto(
    val id: Int?,
    val name: String
)
