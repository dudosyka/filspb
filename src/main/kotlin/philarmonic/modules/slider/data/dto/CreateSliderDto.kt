package philarmonic.modules.slider.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class CreateSliderDto(
    val desktopImage:String? = null,
    val mobileImage:String? = null,
    val link: String? = null,
    val buttonText: String? = null,
    val visible:Boolean,
    val position:Int,
)
