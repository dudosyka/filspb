package philarmonic.modules.review.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class ReviewListDto(
        val id:Int,
    val date:Long,
    val EventName:String,
    val name:String,
    val visible:Boolean,
    val position:Int,
)
