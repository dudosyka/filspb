package philarmonic.modules.partners.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class CreatePartnersDto(
    val name:String,
    val image:String? = null,
    val visible:Boolean,
    val position:Int,
)
