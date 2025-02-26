package philarmonic.modules.people.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PersonClientDto (
    val name: String,
    val position: String,
    val image: String?
)