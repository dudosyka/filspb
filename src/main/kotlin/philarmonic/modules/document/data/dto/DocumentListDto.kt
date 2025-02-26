package philarmonic.modules.document.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class DocumentListDto(
        val id:Int,
    val name:String,
    val visible:Boolean,
    val position:Int,
)
