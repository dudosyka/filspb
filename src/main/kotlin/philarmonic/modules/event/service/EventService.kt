package philarmonic.modules.event.service

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import philarmonic.exceptions.NotFoundException
import philarmonic.modules.event.data.model.EventModel
import philarmonic.modules.event.data.dao.EventDao
import philarmonic.modules.event.data.dto.*
import philarmonic.modules.hall.data.model.HallModel
import philarmonic.modules.hall.data.dao.HallDao
import philarmonic.modules.platform.data.model.PlatformModel
import philarmonic.modules.eventsecond.data.model.EventSecondModel
import philarmonic.modules.eventsecond.data.dao.EventSecondDao
import philarmonic.modules.eventsecond.data.model.EventSecondToTagsModel
import philarmonic.modules.platform.data.dao.PlatformDao
import philarmonic.modules.tag.data.dto.TagClientDto
import philarmonic.modules.tag.data.model.TagModel
import philarmonic.plugins.Logger

class EventService(di: DI) : KodeinService(di) {
    fun create(createDto: CreateEventDto): EventDto = transaction {
        val created = EventDao.new {
            this.hall = HallDao[createDto.hallId ?: 0]
            this.platform = PlatformDao[createDto.platformId ?: 0]
            this.date = createDto.date
            this.time = createDto.time
            this.purchaseLink = createDto.purchaseLink
            this.soldOut = createDto.soldOut
            this.position = createDto.position
            this.event = EventSecondDao[createDto.eventId ?: 0]
            this.visible = createDto.visible
            this.price = createDto.price
        }

        created.toOutputDto()
    }

    fun getAll(): List<EventListDto> = transaction {
        EventModel
            .leftJoin(EventSecondModel)
            .leftJoin(HallModel)
            .leftJoin(PlatformModel)
            .selectAll()
            .orderBy(EventModel.position)
            .map {
                EventListDto(
                    it[EventModel.id].value,
                    it[EventModel.date] ?: 0L,
                    it[EventModel.time] ?: "",
                    it[EventSecondModel.name],
                    it[PlatformModel.name],
                    it[HallModel.name],
                    it[EventModel.visible],
                    it[EventModel.position]
                )
            }
    }

    fun getOne(id: Int): EventDto = transaction {
        EventDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdateEventDto): EventDto = transaction {
        val dao = EventDao[id]
        dao.hall = if (updateDto.hallId == null) dao.hall else HallDao[updateDto.hallId]
        dao.platform = if (updateDto.platformId == null) dao.platform else PlatformDao[updateDto.platformId]
        dao.date = updateDto.date ?: dao.date
        dao.time = updateDto.time ?: dao.time
        dao.purchaseLink = updateDto.purchaseLink ?: dao.purchaseLink
        dao.soldOut = updateDto.soldOut ?: dao.soldOut
        dao.position = updateDto.position ?: dao.position
        dao.event = if (updateDto.eventId == null) dao.event else EventSecondDao[updateDto.eventId]
        dao.visible = updateDto.visible ?: dao.visible
        dao.price = updateDto.price ?: dao.price

        dao.flush()

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = EventDao[id]

        dao.delete()

        true
    }

    fun toggleVisible(id: Int): EventDto = transaction {
        val dao = EventDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): EventDto = transaction {
        val dao = EventDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = EventDao.find { EventModel.position eq prevPosition }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = EventDao.find { EventModel.position eq prevPosition }.firstOrNull()
            prevPosition -= 1
        }

        if (top == null) {
            dao.position = 0
        } else {
            val topPos = top.position
            top.position = dao.position
            dao.position = topPos

            top.flush()
        }

        dao.flush()

        dao.toOutputDto()
    }

    fun positionDown(id: Int): EventDto = transaction {
        val allCount = EventDao.all().count()
        val dao = EventDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = EventDao.find { EventModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = EventDao.find { EventModel.position eq (dao.position + 1) }.firstOrNull()
            prevPosition += 1
        }

        if (bottom == null) {
            dao.position = (allCount - 1).toInt()
        } else {
            val bottomPos = bottom.position
            bottom.position = dao.position
            dao.position = bottomPos

            bottom.flush()
        }

        dao.flush()

        dao.toOutputDto()
    }

    private fun clientBaseQuery(): Query {
        return EventModel
                .leftJoin(HallModel)
                .leftJoin(PlatformModel)
                .leftJoin(EventSecondModel)
                .leftJoin(EventSecondToTagsModel)
                .leftJoin(TagModel)
                .selectAll()
    }

    private fun ResultRow.buildEventClientDto(): EventClientDto =
        EventClientDto(
            this[EventModel.id].value,
            this[EventSecondModel.id].value,
            this[EventModel.date] ?: 0L,
            this[EventModel.time] ?: "",
            this[EventSecondModel.duration] ?: "",
            this[EventSecondModel.image] ?: "",
            this[EventSecondModel.name],
            this[EventSecondModel.shortDescription] ?: "",
            this[EventSecondModel.description] ?: "",
            this[PlatformModel.name],
            this[PlatformModel.address] ?: "",
            this[EventSecondModel.authors],
            if (this[TagModel.id] != null) mutableListOf(
                TagClientDto(
                    this[TagModel.id].value,
                    this[TagModel.name]
                )
            ) else mutableListOf(),
            this[EventModel.purchaseLink] ?: "",
            this[EventModel.price] ?: "",
            this[EventModel.soldOut] ?: false
        )

    fun getOneClient(id: Int): EventClientDto = transaction {
        val result = clientBaseQuery().where {
            (EventModel.id eq id) and (EventModel.visible eq true)
        }
        val event = (result.firstOrNull() ?: throw NotFoundException("Event not found")).buildEventClientDto()

        result.forEachIndexed {
            index, row ->
                if (index == 0) return@forEachIndexed
                event.tags += TagClientDto(
                    row[TagModel.id].value,
                    row[TagModel.name]
                )
        }

        event
    }
    private fun buildEventList(query: Query) = transaction {
        var cur: Int? = null
        val eventList = mutableListOf<EventClientDto>()

        query
            .forEach {
                val curId = it[EventModel.id].value
                if (cur == null || curId != cur) {
                    eventList += it.buildEventClientDto()
                    cur = curId
                } else {
                    eventList.last().tags += TagClientDto(
                        it[TagModel.id].value,
                        it[TagModel.name]
                    )
                }
            }

        eventList.distinctBy { it.id }
    }

    fun getLast(count: Int): List<EventClientDto> = transaction {

        val limited = EventModel
            .select(EventModel.id)
            .orderBy(EventModel.date, SortOrder.DESC)
            .limit(count)
            .map { it[EventModel.id].value }

        buildEventList(
            clientBaseQuery()
                .orderBy(EventModel.date, SortOrder.DESC)
                .where {
                    (EventModel.visible eq true) and
                            (EventModel.id inList limited)
                }
        )
    }

    fun getByPeriod(start: Long, end: Long): List<EventClientDto> = transaction {
        buildEventList(
            clientBaseQuery()
                .orderBy(EventModel.date, SortOrder.DESC)
                .where {
                    (EventModel.date greaterEq start) and
                    (EventModel.date lessEq end) and
                    (EventModel.visible eq true)
                }
        )
    }
}