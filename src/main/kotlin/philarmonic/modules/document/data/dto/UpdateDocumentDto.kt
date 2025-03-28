package philarmonic.modules.document.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class UpdateDocumentDto(
    val name:String?,
    val doc:String?,
    val docName:String?,
    val visible:Boolean?,
    val position:Int?,
)
