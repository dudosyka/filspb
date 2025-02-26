package philarmonic.modules.seasonevent.data.model

import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.default
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.nullable
import philarmonic.utils.database.BaseIntIdTable

object SeasonEventModel: BaseIntIdTable() {
    val name = text("name")
    val image = text("image").nullable().default(null)
    val shortDescription = text("shortDescription").nullable().default(null)
    val description = text("description").nullable().default(null)
    val purchaseLink = text("purchaseLink").nullable().default(null)
    val isActive = bool("isActive").nullable().default(null)
    val position = integer("position")
    val visible = bool("visible")
    val price = text("price").nullable().default(null)
}