package philarmonic.modules.platform.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlatformClientDto (
    val id: Int,
    val name: String
)