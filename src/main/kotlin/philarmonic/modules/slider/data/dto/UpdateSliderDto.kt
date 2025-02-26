package philarmonic.modules.slider.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class UpdateSliderDto(
    val desktopImage:String?,
    val mobileImage:String?,
    val link: String?,
    val buttonText: String?,
    val visible:Boolean?,
    val position:Int?,
)
