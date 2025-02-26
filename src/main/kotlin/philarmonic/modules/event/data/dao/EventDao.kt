package philarmonic.modules.event.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.event.data.dto.EventDto
import philarmonic.modules.event.data.model.EventModel
import philarmonic.modules.hall.data.dao.HallDao
import philarmonic.modules.eventsecond.data.dao.EventSecondDao
import philarmonic.modules.platform.data.dao.PlatformDao

class EventDao(id: EntityID<Int>) : BaseIntEntity<EventDto>(id, EventModel) {
    companion object : BaseIntEntityClass<EventDto, EventDao>(EventModel)

    var hall by HallDao optionalReferencedOn EventModel.hallId
    var hallId by EventModel.hallId
    var platform by PlatformDao optionalReferencedOn EventModel.platformId
    var platformId by EventModel.platformId
    var date by EventModel.date
    var time by EventModel.time
    var purchaseLink by EventModel.purchaseLink
    var soldOut by EventModel.soldOut
    var position by EventModel.position
    var event by EventSecondDao optionalReferencedOn  EventModel.eventId
    var eventId by EventModel.eventId
    var visible by EventModel.visible
    var price by EventModel.price

    override fun toOutputDto(): EventDto =
    EventDto(
        this.id.value,
        this.hallId?.value,
        this.platformId?.value,
        this.date,
        this.time,
        this.purchaseLink,
        this.soldOut,
        this.position,
        this.eventId?.value,
        this.visible,
        this.price,
    )
}