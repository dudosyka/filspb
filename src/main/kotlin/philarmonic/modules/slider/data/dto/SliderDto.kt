package philarmonic.modules.slider.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class SliderDto(
    val id: Int,
    val desktopImage: String?,
    val mobileImage: String?,
    val link: String?,
    val buttonText: String?,
    val visible:Boolean,
    val position:Int,
)
