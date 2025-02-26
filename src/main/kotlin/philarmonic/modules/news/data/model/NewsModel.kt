package philarmonic.modules.news.data.model


import org.jetbrains.exposed.sql.ReferenceOption
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.default
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.nullable
import philarmonic.utils.database.BaseIntIdTable

object NewsModel: BaseIntIdTable() {
    val date = long("date").nullable().default(null)
    val name = text("name")
    val image = text("image").nullable().default(null)
    val shortDescription = text("shortDescription").nullable().default(null)
    val description = text("description").nullable().default(null)
    val visible = bool("visible")
    val position = integer("position")
}