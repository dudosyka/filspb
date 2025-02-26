package philarmonic.modules.seasonevent.service

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import philarmonic.exceptions.NotFoundException
import philarmonic.modules.event.data.model.EventModel
import philarmonic.modules.seasonevent.data.model.SeasonEventModel
import philarmonic.modules.seasonevent.data.dao.SeasonEventDao
import philarmonic.modules.seasonevent.data.dto.*
import philarmonic.modules.hall.data.model.HallModel
import philarmonic.modules.hall.data.dao.HallDao
import philarmonic.modules.platform.data.model.PlatformModel
import philarmonic.modules.eventsecond.data.model.EventSecondModel
import philarmonic.modules.eventsecond.data.dao.EventSecondDao
import philarmonic.modules.eventsecond.data.dto.EventSecondListDto
import philarmonic.modules.eventsecond.data.model.EventSecondToTagsModel
import philarmonic.modules.seasonevent.data.model.SeasonEventToEventModel
import philarmonic.modules.tag.data.dto.TagClientDto
import philarmonic.modules.tag.data.model.TagModel
import philarmonic.utils.database.idValue

class SeasonEventService(di: DI) : KodeinService(di) {
    fun create(createDto: CreateSeasonEventDto): SeasonEventDto = transaction {
        val created = SeasonEventDao.new {
            this.name = createDto.name
            this.image = createDto.image
            this.shortDescription = createDto.shortDescription
            this.description = createDto.description
            this.purchaseLink = createDto.purchaseLink
            this.isActive = createDto.isActive
            this.position = createDto.position
            this.visible = createDto.visible
            this.price = createDto.price
        }

        SeasonEventToEventModel.deleteWhere { seasonEvent eq created.idValue }
        SeasonEventToEventModel.batchInsert(createDto.events) {
            this[SeasonEventToEventModel.seasonEvent] = created.idValue
            this[SeasonEventToEventModel.secondEvent] = it
        }

        created.toOutputDto()
    }

    fun getAll(): List<SeasonEventListDto> = transaction {
        SeasonEventModel
            .select(
                SeasonEventModel.id, SeasonEventModel.name,
                SeasonEventModel.isActive, SeasonEventModel.visible,
                SeasonEventModel.position
            )
            .orderBy(SeasonEventModel.position)
            .map {
                SeasonEventListDto(
                    it[SeasonEventModel.id].value,
                    it[SeasonEventModel.name],
                    it[SeasonEventModel.isActive] ?: true,
                    it[SeasonEventModel.visible],
                    it[SeasonEventModel.position]
                )
            }
    }

    fun getOne(id: Int): SeasonEventDto = transaction {
        SeasonEventDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdateSeasonEventDto): SeasonEventDto = transaction {
        val dao = SeasonEventDao[id]

        if (updateDto.events != null) {
            SeasonEventToEventModel.deleteWhere { SeasonEventToEventModel.seasonEvent eq id }
            SeasonEventToEventModel.batchInsert(updateDto.events) {
                this[SeasonEventToEventModel.seasonEvent] = id
                this[SeasonEventToEventModel.secondEvent] = it
            }
        }

        dao.name = updateDto.name ?: dao.name
        dao.image = updateDto.image ?: dao.image
        dao.shortDescription = updateDto.shortDescription ?: dao.shortDescription
        dao.description = updateDto.description ?: dao.description
        dao.purchaseLink = updateDto.purchaseLink ?: dao.purchaseLink
        dao.isActive = updateDto.isActive
        dao.position = updateDto.position ?: dao.position
        dao.visible = updateDto.visible ?: dao.visible
        dao.price = updateDto.price ?: dao.price

        dao.flush()

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = SeasonEventDao[id]

        dao.delete()

        true
    }

    fun toggleVisible(id: Int): SeasonEventDto = transaction {
        val dao = SeasonEventDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): SeasonEventDto = transaction {
        val dao = SeasonEventDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = SeasonEventDao.find { SeasonEventModel.position eq prevPosition }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = SeasonEventDao.find { SeasonEventModel.position eq prevPosition }.firstOrNull()
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

    fun positionDown(id: Int): SeasonEventDto = transaction {
        val allCount = SeasonEventDao.all().count()
        val dao = SeasonEventDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = SeasonEventDao.find { SeasonEventModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = SeasonEventDao.find { SeasonEventModel.position eq (dao.position + 1) }.firstOrNull()
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
        return SeasonEventModel
                .selectAll()
    }

    private fun ResultRow.buildSeasonEventClientDto() = transaction {
        val secondEventIds = SeasonEventToEventModel
            .leftJoin(EventSecondModel)
            .select(SeasonEventToEventModel.secondEvent, EventSecondModel.name)
            .where { SeasonEventToEventModel.seasonEvent eq this@buildSeasonEventClientDto[SeasonEventModel.id].value }
            .map {
                SeasonEventItemDto(it[SeasonEventToEventModel.secondEvent].value, it[EventSecondModel.name])
            }

        val events = secondEventIds.map { event ->
            EventModel
                .select(EventModel.id)
                .where { EventModel.eventId eq event.id }
                .orderBy(EventModel.date, SortOrder.DESC)
                .limit(1)
                .map {
                    SeasonEventItemDto(it[EventModel.id].value, event.name)
                }
                .ifEmpty {
                    listOf(SeasonEventItemDto(null, event.name))
                }
        }.flatten()

        SeasonEventClientDto(
            this@buildSeasonEventClientDto[SeasonEventModel.id].value,
            this@buildSeasonEventClientDto[SeasonEventModel.image],
            this@buildSeasonEventClientDto[SeasonEventModel.name],
            this@buildSeasonEventClientDto[SeasonEventModel.shortDescription] ?: "",
            this@buildSeasonEventClientDto[SeasonEventModel.description] ?: "",
            this@buildSeasonEventClientDto[SeasonEventModel.purchaseLink] ?: "",
            this@buildSeasonEventClientDto[SeasonEventModel.price] ?: "",
            this@buildSeasonEventClientDto[SeasonEventModel.isActive] ?: true,
            events
        )
    }

    fun getOneClient(id: Int): SeasonEventClientDto = transaction {
        val event = clientBaseQuery().where { (SeasonEventModel.id eq id) and (SeasonEventModel.visible eq true) }.firstOrNull() ?: throw NotFoundException("")
        event.buildSeasonEventClientDto()
    }

    fun getLast(count: Int): List<SeasonEventClientDto> = transaction {
        clientBaseQuery()
            .orderBy(SeasonEventModel.isActive, SortOrder.DESC)
            .where { SeasonEventModel.visible eq true }
            .limit(count)
            .map {
                it.buildSeasonEventClientDto()
            }
    }

    fun getAllClient(): List<SeasonEventClientDto> = transaction {
        clientBaseQuery()
            .orderBy(SeasonEventModel.isActive, SortOrder.DESC)
            .where { SeasonEventModel.visible eq true }
            .map {
                it.buildSeasonEventClientDto()
            }
    }
}