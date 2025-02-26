package philarmonic.modules.event.data.dto


import kotlinx.serialization.Serializable
import philarmonic.modules.tag.data.dto.TagClientDto
import philarmonic.modules.tag.data.dto.TagListDto

@Serializable
data class EventClientDto(
    val id: Int,
    val originalEventId: Int,
    val date: Long,
    val time: String,
    val eventTime: String,
    val image: String,
    val name: String,
    val shortDescription: String,
    val description: String,
    val platformName: String,
    val platformAddress: String,
    val authors: String,
    val tags: MutableList<TagClientDto>,
    val purchaseLink: String,
    val price: String,
    val soldOut: Boolean
)
