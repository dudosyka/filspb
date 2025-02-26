package philarmonic.modules.document.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class CreateDocumentDto(
    val name:String,
    val doc:String? = null,
    val docName:String? = null,
    val visible:Boolean,
    val position:Int,
)
