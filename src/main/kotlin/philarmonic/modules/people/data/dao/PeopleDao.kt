package philarmonic.modules.people.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.people.data.dto.PeopleDto
import philarmonic.modules.people.data.model.PeopleModel
import philarmonic.modules.peoplecategory.data.dao.PeopleCategoryDao
import philarmonic.utils.database.idValue


class PeopleDao(id: EntityID<Int>) : BaseIntEntity<PeopleDto>(id, PeopleModel) {
    companion object : BaseIntEntityClass<PeopleDto, PeopleDao>(PeopleModel)

    var name by PeopleModel.name
    var photo by PeopleModel.photo
    var workPosition by PeopleModel.workPosition
    var category by PeopleCategoryDao optionalReferencedOn PeopleModel.category
    var visible by PeopleModel.visible
    var position by PeopleModel.position

    override fun toOutputDto(): PeopleDto =
    PeopleDto(
        this.id.value,
        this.name,
        this.photo,
        this.workPosition,
        this.category?.idValue,
        this.visible,
        this.position,
    )
}