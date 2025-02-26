package philarmonic.modules.childrenprograms.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class CreateChildrenProgramsDto(
    val name:String,
    val image:String? = null,
    val description:String? = null,
    val visible:Boolean,
    val position:Int,
)
