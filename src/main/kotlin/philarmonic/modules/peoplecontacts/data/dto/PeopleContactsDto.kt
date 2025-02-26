package philarmonic.modules.peoplecontacts.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class PeopleContactsDto(
    val id: Int,
    val surname: String?,
    val io: String?,
    val email: String?,
    val photo: String?,
    val workPosition: String?,
    val visible:Boolean,
    val position:Int,
)
