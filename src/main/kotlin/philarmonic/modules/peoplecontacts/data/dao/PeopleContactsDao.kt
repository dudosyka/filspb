package philarmonic.modules.peoplecontacts.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.peoplecontacts.data.dto.PeopleContactsDto
import philarmonic.modules.peoplecontacts.data.model.PeopleContactsModel


class PeopleContactsDao(id: EntityID<Int>) : BaseIntEntity<PeopleContactsDto>(id, PeopleContactsModel) {
    companion object : BaseIntEntityClass<PeopleContactsDto, PeopleContactsDao>(PeopleContactsModel)

    var surname by PeopleContactsModel.surname
    var io by PeopleContactsModel.io
    var email by PeopleContactsModel.email
    var photo by PeopleContactsModel.photo
    var workPosition by PeopleContactsModel.workPosition
    var visible by PeopleContactsModel.visible
    var position by PeopleContactsModel.position

    override fun toOutputDto(): PeopleContactsDto =
    PeopleContactsDto(
        this.id.value,
        this.surname,
        this.io,
        this.email,
        this.photo,
        this.workPosition,
        this.visible,
        this.position,
    )
}