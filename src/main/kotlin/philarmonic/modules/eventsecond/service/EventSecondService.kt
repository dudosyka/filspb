package philarmonic.modules.eventsecond.service

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.exceptions.BadRequestException
import philarmonic.modules.event.data.model.EventModel
import philarmonic.modules.eventsecond.data.dto.CreateEventSecondDto
import philarmonic.modules.eventsecond.data.dto.UpdateEventSecondDto
import philarmonic.modules.eventsecond.data.dto.EventSecondDto
import philarmonic.modules.eventsecond.data.dto.EventSecondListDto
import philarmonic.modules.eventsecond.data.model.EventSecondModel
import philarmonic.modules.eventsecond.data.dao.EventSecondDao
import philarmonic.modules.eventsecond.data.model.EventSecondToTagsModel
import philarmonic.modules.files.FilesService
import philarmonic.modules.news.data.model.NewsModel
import philarmonic.plugins.Logger
import philarmonic.utils.database.idValue
import philarmonic.utils.files.FilesUtil
import java.lang.System.currentTimeMillis

class EventSecondService(di: DI) : KodeinService(di) {
    private val fileService: FilesService by instance()
    private val fileModule = "events"
    private fun setTags(id: Int, tags: List<Int>) = transaction {
        EventSecondToTagsModel.deleteWhere { eventSecondId eq id }
        EventSecondToTagsModel.batchInsert(tags) {
            this[EventSecondToTagsModel.eventSecondId] = id
            this[EventSecondToTagsModel.tagId] = it
        }
    }

    fun create(createDto: CreateEventSecondDto): EventSecondDto = transaction {
        val imageName = FilesUtil.buildName(createDto.imageName ?: throw BadRequestException("Bad filename"), fileModule)

        val created = EventSecondDao.new {
            this.name = createDto.name
            this.image = imageName
            this.shortDescription = createDto.shortDescription ?: ""
            this.duration = createDto.duration ?: ""
            this.description = createDto.description ?: ""
            this.authors = createDto.authors ?: "[]"
            this.visible = createDto.visible
            this.position = createDto.position
        }

        setTags(created.idValue, createDto.tags)

        FilesUtil.upload(with(createDto.image) {
            if (this == null)
                throw BadRequestException("Bad file")
            this.split("base64,")[1]
        } , imageName)

        created.toOutputDto()
    }

    fun getAll(): List<EventSecondListDto> = transaction {
        EventSecondModel
            .selectAll()
            .orderBy(EventSecondModel.position)
            .map {
                EventSecondListDto(
                    it[EventSecondModel.id].value,
                    it[EventSecondModel.name],
                    it[EventSecondModel.visible],
                    it[EventSecondModel.position]
                )
            }
    }

    fun getOne(id: Int): EventSecondDto = transaction {
        EventSecondDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdateEventSecondDto): EventSecondDto = transaction {
        val dao = EventSecondDao[id]
        dao.name = updateDto.name ?: dao.name
        val oldImageName = if (updateDto.image != null && updateDto.imageName != null) {
            val fileName = FilesUtil.buildName(updateDto.imageName, fileModule)
            val old = dao.image
            dao.image = fileName
            FilesUtil.upload(updateDto.image.split("base64,")[1], fileName)
            old
        } else null
        dao.shortDescription = updateDto.shortDescription ?: dao.shortDescription
        dao.duration = updateDto.duration ?: dao.duration
        dao.description = updateDto.description ?: dao.description
        dao.authors = updateDto.authors ?: dao.authors
        dao.visible = updateDto.visible ?: dao.visible
        dao.position = updateDto.position ?: dao.position

        if (updateDto.tags != null) {
            setTags(id, updateDto.tags)
        }

        dao.flush()

        if (updateDto.image != null && updateDto.imageName != null && oldImageName != null) {
            FilesUtil.removeFile(oldImageName)
        }

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = EventSecondDao[id]

        dao.delete()

        if (dao.image != null)
            FilesUtil.removeFile(dao.image!!)

        true
    }

    fun toggleVisible(id: Int): EventSecondDto = transaction {
        val dao = EventSecondDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): EventSecondDto = transaction {
        val dao = EventSecondDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = EventSecondDao.find { EventSecondModel.position eq prevPosition }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = EventSecondDao.find { EventSecondModel.position eq prevPosition }.firstOrNull()
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

    fun positionDown(id: Int): EventSecondDto = transaction {
        val allCount = EventSecondDao.all().count()
        val dao = EventSecondDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = EventSecondDao.find { EventSecondModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = EventSecondDao.find { EventSecondModel.position eq (dao.position + 1) }.firstOrNull()
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

    fun getRepertoire(): List<EventSecondDto> = transaction {
        val currentEvents = EventModel.select(EventModel.eventId).mapNotNull { it[EventModel.eventId]?.value }
        EventSecondModel
            .selectAll()
            .where { (EventSecondModel.id notInList currentEvents) and (EventSecondModel.visible eq true) }
            .map {
                EventSecondDao.wrapRow(it).toOutputDto()
            }
    }

    fun updateOldBase64(): String = transaction {
        EventSecondModel
            .select(EventSecondModel.id, EventSecondModel.image)
            .where {
                EventSecondModel.image notLike "/uploads/%"
            }
            .map {
                val base64 = it[EventSecondModel.image]?.split("base64,")?.get(1)
                val imageName = FilesUtil.buildName("${it[EventSecondModel.id]}_${currentTimeMillis()}", fileModule) + "jpeg"
                if (base64 == null) return@map
                FilesUtil.upload(base64, imageName)
                EventSecondModel.update({ EventSecondModel.id eq it[EventSecondModel.id].value }) { update ->
                    update[image] = imageName
                }
            }

        ""
    }
}