package philarmonic.modules.news.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class UpdateNewsDto(
    val date:Long? = null,
    val name:String? = null,
    val image:String? = null,
    val imageName:String? = null,
    val shortDescription:String? = null,
    val description:String? = null,
    val visible:Boolean? = null,
    val position:Int? = null,
)
