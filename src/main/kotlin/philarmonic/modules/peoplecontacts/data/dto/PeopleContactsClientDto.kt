package philarmonic.modules.peoplecontacts.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PeopleContactsClientDto (
    val name: String,
    val position: String,
    val image: String,
    val email: String
)