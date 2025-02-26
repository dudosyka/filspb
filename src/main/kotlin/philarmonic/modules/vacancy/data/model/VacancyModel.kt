package philarmonic.modules.vacancy.data.model


import org.jetbrains.exposed.sql.ReferenceOption
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.default
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.nullable
import philarmonic.utils.database.BaseIntIdTable

object VacancyModel: BaseIntIdTable() {
    val name = text("name")
    val image = text("image").nullable().default(null)
    val description = text("description").nullable().default(null)
    val visible = bool("visible")
    val position = integer("position")
}