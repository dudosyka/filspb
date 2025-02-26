package philarmonic.modules.people.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class PeopleByCategoryDto(
    val categoryName: String,
    val people: List<PersonClientDto>
)
