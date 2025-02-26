package philarmonic.modules.event.data.model


import philarmonic.modules.hall.data.model.HallModel
import philarmonic.modules.eventsecond.data.model.EventSecondModel
import org.jetbrains.exposed.sql.ReferenceOption
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.default
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.nullable
import philarmonic.modules.hall.data.model.HallModel.default
import philarmonic.modules.hall.data.model.HallModel.nullable
import philarmonic.modules.platform.data.model.PlatformModel
import philarmonic.utils.database.BaseIntIdTable

object EventModel: BaseIntIdTable() {
    val platformId = reference("platform", PlatformModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE).nullable().default(null)
    val hallId = reference("hallId", HallModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE).nullable().default(null)
    val date = long("date").nullable().default(null)
    val time = text("time").nullable().default(null)
    val purchaseLink = text("purchaseLink").nullable().default(null)
    val soldOut = bool("soldOut").nullable().default(null)
    val position = integer("position")
    val eventId = reference("eventId", EventSecondModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE).nullable().default(null)
    val visible = bool("visible")
    val price = text("price").nullable().default(null)
}