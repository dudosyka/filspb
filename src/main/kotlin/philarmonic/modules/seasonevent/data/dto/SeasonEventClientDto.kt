package philarmonic.modules.seasonevent.data.dto


import kotlinx.serialization.Serializable
import philarmonic.modules.tag.data.dto.TagClientDto

@Serializable
data class SeasonEventClientDto(
    val id: Int,
    val image: String?,
    val name: String,
    val shortDescription: String,
    val description: String,
    val purchaseLink: String,
    val price: String,
    val isActive: Boolean,
    val events: List<SeasonEventItemDto>
)
