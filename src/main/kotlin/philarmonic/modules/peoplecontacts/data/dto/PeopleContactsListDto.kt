package philarmonic.modules.peoplecontacts.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class PeopleContactsListDto(
    val id:Int,
    val surname:String,
    val workPosition:String,
    val visible:Boolean,
    val position:Int,
)
