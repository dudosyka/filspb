package philarmonic.modules.peoplecontacts.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class CreatePeopleContactsDto(
    val surname:String? = null,
    val io:String? = null,
    val email:String? = null,
    val photo:String? = null,
    val workPosition:String? = null,
    val visible:Boolean,
    val position:Int,
)
