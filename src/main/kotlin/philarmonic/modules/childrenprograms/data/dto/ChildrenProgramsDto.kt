package philarmonic.modules.childrenprograms.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class ChildrenProgramsDto(
    val id: Int,
    val name:String,
    val image:String?,
    val description: String?,
    val visible:Boolean,
    val position:Int,
)
