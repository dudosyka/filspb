package philarmonic.modules.departmentscontacts.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.departmentscontacts.data.dto.DepartmentsContactsDto
import philarmonic.modules.departmentscontacts.data.model.DepartmentsContactsModel


class DepartmentsContactsDao(id: EntityID<Int>) : BaseIntEntity<DepartmentsContactsDto>(id, DepartmentsContactsModel) {
    companion object : BaseIntEntityClass<DepartmentsContactsDto, DepartmentsContactsDao>(DepartmentsContactsModel)

    var name by DepartmentsContactsModel.name
    var phone by DepartmentsContactsModel.phone
    var timeTable by DepartmentsContactsModel.timeTable
    var visible by DepartmentsContactsModel.visible
    var position by DepartmentsContactsModel.position

    override fun toOutputDto(): DepartmentsContactsDto =
    DepartmentsContactsDto(
        this.id.value,
        this.name,
        this.phone,
        this.timeTable,
        this.visible,
        this.position,
    )
}