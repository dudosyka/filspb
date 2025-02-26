package philarmonic.modules.partners.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class PartnersListDto(
    val id:Int,
    val name:String,
    val image:String,
    val visible:Boolean,
    val position:Int,
)
