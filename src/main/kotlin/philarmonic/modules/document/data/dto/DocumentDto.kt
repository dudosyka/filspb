package philarmonic.modules.document.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class DocumentDto(
    val id: Int,
    val name:String,
    val doc: String?,
    val visible:Boolean,
    val position:Int,
)
