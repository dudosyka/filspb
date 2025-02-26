package philarmonic.modules.seasonevent.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class UpdateSeasonEventDto(
    val name: String?,
    val image: String?,
    val shortDescription: String?,
    val description: String?,
    val purchaseLink: String?,
    val isActive: Boolean,
    val position: Int?,
    val events: List<Int>?,
    val visible: Boolean?,
    val price: String?,
)
