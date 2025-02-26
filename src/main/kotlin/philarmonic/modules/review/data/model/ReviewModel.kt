package philarmonic.modules.review.data.model


import philarmonic.modules.event.data.model.EventModel
import org.jetbrains.exposed.sql.ReferenceOption
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.default
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.nullable
import philarmonic.modules.eventsecond.data.model.EventSecondModel
import philarmonic.utils.database.BaseIntIdTable

object ReviewModel: BaseIntIdTable() {
    val date = long("date").nullable().default(null)
    val name = text("name")
    val review = text("review").nullable().default(null)
    val eventId = reference("eventId", EventSecondModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE).nullable().default(null)
    val visible = bool("visible").default(true)
    val position = integer("position").default(1)
}