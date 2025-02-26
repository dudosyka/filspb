package philarmonic.modules.platform.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class UpdatePlatformDto(
    
    val name:String?,
    val address:String?,
    val visible:Boolean?,
    val position:Int?,
)
