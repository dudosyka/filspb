package philarmonic.modules.hall.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class UpdateHallDto(
    val name:String?,
    val visible:Boolean?,
    val position:Int?,
)
