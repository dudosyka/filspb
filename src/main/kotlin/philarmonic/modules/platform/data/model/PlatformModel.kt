package philarmonic.modules.platform.data.model


import org.jetbrains.exposed.sql.ReferenceOption
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.default
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.nullable
import philarmonic.utils.database.BaseIntIdTable

object PlatformModel: BaseIntIdTable() {
    val name = text("name")
    val address = text("address").nullable().default(null)
    val visible = bool("visible")
    val position = integer("position")
}