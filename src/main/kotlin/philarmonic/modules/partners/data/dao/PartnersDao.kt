package philarmonic.modules.partners.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.partners.data.dto.PartnersDto
import philarmonic.modules.partners.data.model.PartnersModel


class PartnersDao(id: EntityID<Int>) : BaseIntEntity<PartnersDto>(id, PartnersModel) {
    companion object : BaseIntEntityClass<PartnersDto, PartnersDao>(PartnersModel)

    var name by PartnersModel.name
    var image by PartnersModel.image
    var visible by PartnersModel.visible
    var position by PartnersModel.position

    override fun toOutputDto(): PartnersDto =
    PartnersDto(
        this.id.value,
        this.name,
        this.image,
        this.visible,
        this.position,
    )
}