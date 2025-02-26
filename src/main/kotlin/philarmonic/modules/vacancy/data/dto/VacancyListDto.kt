package philarmonic.modules.vacancy.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class VacancyListDto(
        val id:Int,
    val name:String,
    val visible:Boolean,
    val position:Int,
)
