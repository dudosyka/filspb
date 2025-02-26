package philarmonic.modules.vacancy.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class VacancyDto(
    val id: Int,
    val name:String,
    val image:String?,
    val description:String?,
    val visible:Boolean,
    val position:Int,
)
