package philarmonic.modules.news.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class NewsListDto(
        val id:Int,
    val date:Long,
    val name:String,
    val visible:Boolean,
    val position:Int,
)
