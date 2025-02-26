package philarmonic.modules.tag.data.model


import org.jetbrains.exposed.sql.ReferenceOption
import philarmonic.utils.database.BaseIntIdTable

object TagModel: BaseIntIdTable() {
    val name = text("name")
    val visible = bool("visible")
    val position = integer("position")
}