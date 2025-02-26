package philarmonic.modules.slider.data.model


import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.default
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.nullable
import philarmonic.utils.database.BaseIntIdTable

object SliderModel: BaseIntIdTable() {
    val desktopImage = text("desktopImage").nullable().default(null)
    val mobileImage = text("mobileImage").nullable().default(null)
    val link = text("link").nullable().default(null)
    val buttonText = text("buttonText").nullable().default(null)
    val visible = bool("visible")
    val position = integer("position")
}