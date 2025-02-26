package philarmonic.modules.people.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class CreatePeopleDto(
    val photo:String? = null,
    val name:String? = null,
    val workPosition:String? = null,
    val category:Int? = null,
    val visible:Boolean,
    val position:Int,
)
