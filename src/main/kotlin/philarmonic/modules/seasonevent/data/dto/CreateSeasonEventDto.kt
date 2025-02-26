package philarmonic.modules.seasonevent.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class CreateSeasonEventDto(
    val name: String,
    val image: String? = null,
    val shortDescription: String? = null,
    val description: String? = null,
    val purchaseLink:String? = null,
    val isActive: Boolean? = null,
    val position:Int,
    val events: List<Int> = listOf(),
    val visible:Boolean,
    val price:String? = null,
)
