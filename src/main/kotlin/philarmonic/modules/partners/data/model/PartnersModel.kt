package philarmonic.modules.partners.data.model

import philarmonic.utils.database.BaseIntIdTable

object PartnersModel: BaseIntIdTable() {
    val name = text("name")
    val image = text("image").nullable().default(null)
    val visible = bool("visible")
    val position = integer("position")
}