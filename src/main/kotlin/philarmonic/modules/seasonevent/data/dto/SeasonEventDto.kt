package philarmonic.modules.seasonevent.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class SeasonEventDto(
    val id: Int,
    val name: String,
    val image: String? = null,
    val shortDescription: String?,
    val description: String?,
    val purchaseLink: String?,
    val isActive: Boolean?,
    val position:Int,
    val events: List<Int>,
    val visible:Boolean,
    val price: String?,
)
