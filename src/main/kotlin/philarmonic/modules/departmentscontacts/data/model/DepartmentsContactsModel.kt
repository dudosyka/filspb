package philarmonic.modules.departmentscontacts.data.model


import org.jetbrains.exposed.sql.ReferenceOption
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.default
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.nullable
import philarmonic.utils.database.BaseIntIdTable

object DepartmentsContactsModel: BaseIntIdTable() {
    val name = text("name")
    val phone = text("phone").nullable().default(null)
    val timeTable = text("timeTable").nullable().default(null)
    val visible = bool("visible")
    val position = integer("position")
}