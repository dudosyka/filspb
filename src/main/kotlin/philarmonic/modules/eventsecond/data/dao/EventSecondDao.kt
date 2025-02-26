package philarmonic.modules.eventsecond.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.modules.eventsecond.data.dto.EventSecondDto
import philarmonic.modules.eventsecond.data.model.EventSecondModel
import philarmonic.modules.eventsecond.data.model.EventSecondToTagsModel
import philarmonic.utils.database.idValue


class EventSecondDao(id: EntityID<Int>) : BaseIntEntity<EventSecondDto>(id, EventSecondModel) {
    companion object : BaseIntEntityClass<EventSecondDto, EventSecondDao>(EventSecondModel)

    var name by EventSecondModel.name
    var image by EventSecondModel.image
    var shortDescription by EventSecondModel.shortDescription
    var duration by EventSecondModel.duration
    var description by EventSecondModel.description
    var authors by EventSecondModel.authors
    var visible by EventSecondModel.visible
    var position by EventSecondModel.position

    val tags: List<Int> get() = transaction {
        EventSecondToTagsModel.select(EventSecondToTagsModel.tagId).where { EventSecondToTagsModel.eventSecondId eq idValue }.map {
            it[EventSecondToTagsModel.tagId].value
        }
    }

    override fun toOutputDto(): EventSecondDto =
    EventSecondDto(
        this.id.value,
        this.name,
        this.image,
        this.shortDescription,
        this.duration,
        this.description,
        this.authors,
        this.tags,
        this.visible,
        this.position,
    )
}