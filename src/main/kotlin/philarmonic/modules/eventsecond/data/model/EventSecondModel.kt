package philarmonic.modules.eventsecond.data.model


import philarmonic.utils.database.BaseIntIdTable

object EventSecondModel: BaseIntIdTable() {
    val name = text("name")
    val image = text("image").nullable().default(null)
    val shortDescription = text("shortDescription").nullable().default(null)
    val duration = text("duration").nullable().default(null)
    val description = text("description").nullable().default(null)
    val authors = text("authors").default("[]")
    val visible = bool("visible")
    val position = integer("position")
}