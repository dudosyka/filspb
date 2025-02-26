package philarmonic.modules.search


import kotlinx.serialization.Serializable

@Serializable
data class SearchResultDto(
    val id: Int,
    val name: String,
    val shortDescription: String,
    val image: String,
    val isArticle: Boolean
)
