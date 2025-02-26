package philarmonic.modules.seasonevent.data.model


import philarmonic.modules.hall.data.model.HallModel
import philarmonic.modules.eventsecond.data.model.EventSecondModel
import org.jetbrains.exposed.sql.ReferenceOption
import philarmonic.modules.eventsecond.data.model.EventSecondModel.default
import philarmonic.modules.eventsecond.data.model.EventSecondModel.nullable
import philarmonic.utils.database.BaseIntIdTable

object SeasonEventToEventModel: BaseIntIdTable() {
    val seasonEvent = reference("seasonEventId", SeasonEventModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val secondEvent = reference("secondEventId", EventSecondModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
}