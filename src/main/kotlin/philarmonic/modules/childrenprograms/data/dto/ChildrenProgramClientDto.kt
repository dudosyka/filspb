package philarmonic.modules.childrenprograms.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class ChildrenProgramClientDto(
    val name: String,
    val description: String?,
    val image: String
)
