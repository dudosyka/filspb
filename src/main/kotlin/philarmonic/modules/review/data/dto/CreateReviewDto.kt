package philarmonic.modules.review.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateReviewDto(
    val date:Long? = null,
    val name:String? = null,
    val review:String? = null,
    val eventId:Int? = null,
    val visible:Boolean = true,
    val position:Int = 1,
)
