package philarmonic.modules.news.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class CreateNewsDto(
    val date:Long? = null,
    val name:String,
    val image:String? = null,
    val imageName:String? = null,
    val shortDescription:String? = null,
    val description:String? = null,
    val visible:Boolean,
    val position:Int,
)
