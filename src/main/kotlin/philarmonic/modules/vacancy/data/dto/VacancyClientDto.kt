package philarmonic.modules.vacancy.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class VacancyClientDto(
    val name: String,
    val description: String,
    val image: String
)
