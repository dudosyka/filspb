package philarmonic.modules.platform.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class PlatformListDto(
    val id:Int,
    val name:String,
    val address:String,
    val visible:Boolean,
    val position:Int,
)
