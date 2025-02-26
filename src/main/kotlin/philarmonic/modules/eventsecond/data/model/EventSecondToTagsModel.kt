package philarmonic.modules.eventsecond.data.model


import org.jetbrains.exposed.sql.ReferenceOption
import philarmonic.modules.tag.data.model.TagModel
import philarmonic.utils.database.BaseIntIdTable

object EventSecondToTagsModel: BaseIntIdTable() {
    val eventSecondId = reference("event_second_id", EventSecondModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val tagId = reference("tag_id", TagModel, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
}