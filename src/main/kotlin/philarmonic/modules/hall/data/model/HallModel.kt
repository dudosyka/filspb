package philarmonic.modules.hall.data.model


import philarmonic.modules.platform.data.model.PlatformModel
import org.jetbrains.exposed.sql.ReferenceOption
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.default
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.nullable
import philarmonic.utils.database.BaseIntIdTable

object HallModel: BaseIntIdTable() {
    val name = text("name")
    val visible = bool("visible")
    val position = integer("position")
}