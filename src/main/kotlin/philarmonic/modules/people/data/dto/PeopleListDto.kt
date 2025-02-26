package philarmonic.modules.people.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class PeopleListDto(
    val id:Int,
    val name:String,
    val visible:Boolean,
    val position:Int,
)
