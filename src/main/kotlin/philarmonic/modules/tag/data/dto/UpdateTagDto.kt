package philarmonic.modules.tag.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class UpdateTagDto(
    
    val name:String?,
    val visible:Boolean?,
    val position:Int?,
)
