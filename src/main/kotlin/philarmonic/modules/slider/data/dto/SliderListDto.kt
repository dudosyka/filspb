package philarmonic.modules.slider.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class SliderListDto(
    val id:Int,
    val desktopImage:String,
    val buttonText: String,
    val visible:Boolean,
    val position:Int,
)
