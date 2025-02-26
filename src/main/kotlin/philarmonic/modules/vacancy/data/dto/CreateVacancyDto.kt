package philarmonic.modules.vacancy.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class CreateVacancyDto(
    val name:String,
    val image:String? = null,
    val description:String? = null,
    val visible:Boolean,
    val position:Int,
)
