package philarmonic.modules.seasonevent.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.seasonevent.data.dto.SeasonEventDto
import philarmonic.modules.seasonevent.data.model.SeasonEventModel
import philarmonic.modules.eventsecond.data.model.EventSecondModel
import philarmonic.modules.seasonevent.data.model.SeasonEventToEventModel
import philarmonic.utils.database.idValue

class SeasonEventDao(id: EntityID<Int>) : BaseIntEntity<SeasonEventDto>(id, SeasonEventModel) {
    companion object : BaseIntEntityClass<SeasonEventDto, SeasonEventDao>(SeasonEventModel)

    var name by SeasonEventModel.name
    var image by SeasonEventModel.image
    var shortDescription by SeasonEventModel.shortDescription
    var description by SeasonEventModel.description
    var purchaseLink by SeasonEventModel.purchaseLink
    var isActive by SeasonEventModel.isActive
    var position by SeasonEventModel.position
    var visible by SeasonEventModel.visible
    var price by SeasonEventModel.price

    val events: List<Int> get() =
        SeasonEventToEventModel
            .leftJoin(EventSecondModel)
            .select(SeasonEventToEventModel.secondEvent, EventSecondModel.name)
            .where { SeasonEventToEventModel.seasonEvent eq idValue }
            .map {
                it[SeasonEventToEventModel.secondEvent].value
            }

    override fun toOutputDto(): SeasonEventDto =
    SeasonEventDto(
        this.id.value,
        name,
        image,
        shortDescription,
        description,
        purchaseLink,
        isActive,
        position,
        events,
        visible,
        price
    )
}