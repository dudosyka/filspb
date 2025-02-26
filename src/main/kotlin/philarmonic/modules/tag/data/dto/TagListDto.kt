package philarmonic.modules.tag.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class TagListDto(
    val id:Int,
    val name:String,
    val visible:Boolean,
    val position:Int,
)
