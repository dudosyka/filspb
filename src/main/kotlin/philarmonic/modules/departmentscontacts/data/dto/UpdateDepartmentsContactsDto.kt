package philarmonic.modules.departmentscontacts.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class UpdateDepartmentsContactsDto(
    
    val name:String?,
    val phone:String?,
    val timeTable:String?,
    val visible:Boolean?,
    val position:Int?,
)
