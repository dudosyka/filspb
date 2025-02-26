package philarmonic.modules.departmentscontacts.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class DepartmentContactsClientDto(
    val name: String,
    val phone: String,
    val timeTable: String
)
