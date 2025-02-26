package philarmonic.modules.vacancy.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.vacancy.data.dto.VacancyDto
import philarmonic.modules.vacancy.data.model.VacancyModel


class VacancyDao(id: EntityID<Int>) : BaseIntEntity<VacancyDto>(id, VacancyModel) {
    companion object : BaseIntEntityClass<VacancyDto, VacancyDao>(VacancyModel)

    var name by VacancyModel.name
    var image by VacancyModel.image
    var description by VacancyModel.description
    var visible by VacancyModel.visible
    var position by VacancyModel.position

    override fun toOutputDto(): VacancyDto =
    VacancyDto(
        this.id.value,
        this.name,
        this.image,
        this.description,
        this.visible,
        this.position,
    )
}