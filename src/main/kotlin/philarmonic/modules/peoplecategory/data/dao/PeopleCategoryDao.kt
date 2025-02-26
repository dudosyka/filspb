package philarmonic.modules.peoplecategory.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.peoplecategory.data.dto.PeopleCategoryDto
import philarmonic.modules.peoplecategory.data.model.PeopleCategoryModel


class PeopleCategoryDao(id: EntityID<Int>) : BaseIntEntity<PeopleCategoryDto>(id, PeopleCategoryModel) {
    companion object : BaseIntEntityClass<PeopleCategoryDto, PeopleCategoryDao>(PeopleCategoryModel)

    var name by PeopleCategoryModel.name
    var visible by PeopleCategoryModel.visible
    var position by PeopleCategoryModel.position

    override fun toOutputDto(): PeopleCategoryDto =
    PeopleCategoryDto(
        this.id.value,
        this.name,
        this.visible,
        this.position,
    )
}