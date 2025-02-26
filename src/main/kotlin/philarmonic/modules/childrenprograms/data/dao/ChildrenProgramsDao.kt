package philarmonic.modules.childrenprograms.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.childrenprograms.data.dto.ChildrenProgramsDto
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel


class ChildrenProgramsDao(id: EntityID<Int>) : BaseIntEntity<ChildrenProgramsDto>(id, ChildrenProgramsModel) {
    companion object : BaseIntEntityClass<ChildrenProgramsDto, ChildrenProgramsDao>(ChildrenProgramsModel)

    var name by ChildrenProgramsModel.name
    var image by ChildrenProgramsModel.image
    var description by ChildrenProgramsModel.description
    var visible by ChildrenProgramsModel.visible
    var position by ChildrenProgramsModel.position

    override fun toOutputDto(): ChildrenProgramsDto =
    ChildrenProgramsDto(
        this.id.value,
        this.name,
        this.image,
        this.description,
        this.visible,
        this.position,
    )
}