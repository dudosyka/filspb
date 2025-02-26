package philarmonic.modules.systemconfiguration.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class CreateSystemConfigurationDto(
    
    val name:String,
    val email:String,
    val address:String,
    val city:String,
    val addressShort:String,
    val vk:String,
    val telegram:String,
    val soundCloud:String,
)
