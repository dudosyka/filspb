package philarmonic.modules.childrenprograms.data.model


import org.jetbrains.exposed.sql.ReferenceOption
import philarmonic.utils.database.BaseIntIdTable

object ChildrenProgramsModel: BaseIntIdTable() {
    val name = text("name")
    val image = text("image").nullable().default(null)
    val description = text("description").nullable().default(null)
    val visible = bool("visible")
    val position = integer("position")
}