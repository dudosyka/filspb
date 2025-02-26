package philarmonic.modules.review.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class UpdateReviewDto(
    
    val date:Long?,
    val name:String?,
    val review:String?,
    val eventId:Int?,
    val visible:Boolean?,
    val position:Int?,
)
