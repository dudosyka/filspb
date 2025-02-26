package philarmonic.modules.departmentscontacts.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class DepartmentsContactsListDto(
        val id:Int,
    val name:String,
    val visible:Boolean,
    val position:Int,
)
