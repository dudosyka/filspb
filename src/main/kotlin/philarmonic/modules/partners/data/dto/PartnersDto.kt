package philarmonic.modules.partners.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class PartnersDto(
    val id: Int,
    val name:String,
    val image: String?,
    val visible:Boolean,
    val position:Int,
)
