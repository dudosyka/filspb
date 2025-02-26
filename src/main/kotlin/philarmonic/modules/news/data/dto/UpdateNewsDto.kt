package philarmonic.modules.news.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class UpdateNewsDto(
    
    val date:Long?,
    val name:String?,
    val image:String?,
    val imageName:String?,
    val shortDescription:String?,
    val description:String?,
    val visible:Boolean?,
    val position:Int?,
)
