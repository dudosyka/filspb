package philarmonic.modules.peoplecontacts.data.model


import org.jetbrains.exposed.sql.ReferenceOption
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.default
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.nullable
import philarmonic.utils.database.BaseIntIdTable

object PeopleContactsModel: BaseIntIdTable() {
    val surname = text("surname").nullable().default(null)
    val io = text("io").nullable().default(null)
    val email = text("email").nullable().default(null)
    val photo = text("photo").nullable().default(null)
    val workPosition = text("workPosition").nullable().default(null)
    val visible = bool("visible")
    val position = integer("position")
}