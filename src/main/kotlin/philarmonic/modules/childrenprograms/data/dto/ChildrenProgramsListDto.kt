package philarmonic.modules.childrenprograms.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class ChildrenProgramsListDto(
    val id:Int,
    val name:String,
    val visible:Boolean,
    val position:Int,
)
