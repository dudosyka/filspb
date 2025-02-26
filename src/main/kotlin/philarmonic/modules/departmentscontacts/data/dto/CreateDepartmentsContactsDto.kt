package philarmonic.modules.departmentscontacts.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class CreateDepartmentsContactsDto(
    val name:String,
    val phone:String? = null,
    val timeTable:String? = null,
    val visible:Boolean,
    val position:Int,
)
