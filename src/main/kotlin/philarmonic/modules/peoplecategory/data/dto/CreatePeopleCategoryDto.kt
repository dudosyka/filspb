package philarmonic.modules.peoplecategory.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class CreatePeopleCategoryDto(
    val name:String,
    val visible:Boolean,
    val position:Int,
)
