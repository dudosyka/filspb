package philarmonic.modules.platform.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class CreatePlatformDto(
    val name:String,
    val address:String? = null,
    val visible:Boolean,
    val position:Int,
)
