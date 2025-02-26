package philarmonic.modules.tag.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class TagClientDto(
    val id: Int,
    val name: String
)
