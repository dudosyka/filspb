package philarmonic.modules.news.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class NewsClientDto(
    val id: Int,
    val name: String,
    val image: String,
    val description: String
)
