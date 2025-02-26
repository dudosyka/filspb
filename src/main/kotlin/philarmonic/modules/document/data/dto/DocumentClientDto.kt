package philarmonic.modules.document.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class DocumentClientDto (
    val name: String,
    val doc: String
)