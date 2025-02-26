package philarmonic.modules.document.data.model


import org.jetbrains.exposed.sql.ReferenceOption
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.default
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.nullable
import philarmonic.utils.database.BaseIntIdTable

object DocumentModel: BaseIntIdTable() {
    val name = text("name")
    val doc = text("doc").nullable().default(null)
    val visible = bool("visible")
    val position = integer("position")
}