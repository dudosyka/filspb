package philarmonic.modules.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAdminDto (
    val id: Int,
    val login: String,
    val pass: String
)