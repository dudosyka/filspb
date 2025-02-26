package philarmonic.modules.people.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class UpdatePeopleDto(
    val name:String?,
    val photo:String?,
    val workPosition:String?,
    val category:Int?,
    val visible:Boolean?,
    val position:Int?,
)
