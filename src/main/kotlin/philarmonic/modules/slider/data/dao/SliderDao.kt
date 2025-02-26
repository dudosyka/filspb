package philarmonic.modules.slider.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.slider.data.dto.SliderDto
import philarmonic.modules.slider.data.model.SliderModel


class SliderDao(id: EntityID<Int>) : BaseIntEntity<SliderDto>(id, SliderModel) {
    companion object : BaseIntEntityClass<SliderDto, SliderDao>(SliderModel)


    var desktopImage by SliderModel.desktopImage
    var mobileImage by SliderModel.mobileImage
    var link by SliderModel.link
    var buttonText by SliderModel.buttonText
    var visible by SliderModel.visible
    var position by SliderModel.position

    override fun toOutputDto(): SliderDto =
    SliderDto(
        this.id.value,
        this.desktopImage,
        this.mobileImage,
        this.link,
        this.buttonText,
        this.visible,
        this.position,
    )
}