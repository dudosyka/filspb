package philarmonic.modules.eventsecond.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class CreateEventSecondDto(
    val name: String,
    val image: String? = null,
    val imageName: String? = null,
    val shortDescription: String? = null,
    val duration: String? = null,
    val description: String? = null,
    val authors: String? = null,
    val tags: List<Int> = listOf(),
    val visible: Boolean,
    val position: Int,
)
