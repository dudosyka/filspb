package philarmonic.modules.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterInputDto (
    val login: String,
    val pass: String
)