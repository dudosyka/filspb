package philarmonic.modules.peoplecategory.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class PeopleCategoryDto(
    val id: Int,
    val name:String,
    val visible:Boolean,
    val position:Int,
)
