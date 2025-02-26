package philarmonic.modules.news.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class NewsListClientDto(
    val id: Int,
    val date: Long,
    val name: String,
    val shortDescription: String,
    val image: String,
)
