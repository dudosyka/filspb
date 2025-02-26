package philarmonic.modules.peoplecategory.data.model


import org.jetbrains.exposed.sql.ReferenceOption
import philarmonic.utils.database.BaseIntIdTable

object PeopleCategoryModel: BaseIntIdTable() {
    val name = text("name")
    val visible = bool("visible")
    val position = integer("position")
}