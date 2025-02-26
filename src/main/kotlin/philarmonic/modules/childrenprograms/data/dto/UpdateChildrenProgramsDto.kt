package philarmonic.modules.childrenprograms.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class UpdateChildrenProgramsDto(
    val name:String?,
    val image:String?,
    val description:String?,
    val visible:Boolean?,
    val position:Int?,
)
