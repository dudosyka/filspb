package philarmonic.modules.hall.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class HallListDto(
    val id:Int,
    val name:String,
    val visible:Boolean,
    val position:Int,
)
