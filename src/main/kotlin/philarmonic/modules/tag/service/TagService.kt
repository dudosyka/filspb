package philarmonic.modules.tag.service

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import philarmonic.exceptions.BadRequestException
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import philarmonic.modules.platform.data.dao.PlatformDao
import philarmonic.modules.platform.data.model.PlatformModel
import philarmonic.modules.tag.data.model.TagModel
import philarmonic.modules.tag.data.dao.TagDao
import philarmonic.modules.tag.data.dto.*
import philarmonic.modules.vacancy.data.model.VacancyModel

class TagService(di: DI) : KodeinService(di) {
    fun create(createDto: CreateTagDto): TagDto = transaction {
        val created = TagDao.new {
            this.name = createDto.name
            this.visible = createDto.visible
            this.position = createDto.position
        }

        created.toOutputDto()
    }

    fun getAll(): List<TagListDto> = transaction {
        TagModel
            .selectAll()
            .orderBy(TagModel.position)
            .map {
                TagListDto(
                    it[TagModel.id].value,
                    it[TagModel.name],
                    it[TagModel.visible],
                    it[TagModel.position]
                )
            }
    }

    fun getOne(id: Int): TagDto = transaction {
        TagDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdateTagDto): TagDto = transaction {
        val dao = TagDao[id]
        dao.name = updateDto.name ?: dao.name
        dao.visible = updateDto.visible ?: dao.visible
        dao.position = updateDto.position ?: dao.position

        dao.flush()

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = TagDao[id]

        dao.delete()

        true
    }

    fun toggleVisible(id: Int): TagDto = transaction {
        val dao = TagDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): TagDto = transaction {
        val dao = TagDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = TagDao.find { TagModel.position eq prevPosition }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = TagDao.find { TagModel.position eq prevPosition }.firstOrNull()
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

    fun positionDown(id: Int): TagDto = transaction {
        val allCount = TagDao.all().count()
        val dao = TagDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = TagDao.find { TagModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = TagDao.find { TagModel.position eq (dao.position + 1) }.firstOrNull()
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

    fun getAllClient(): List<TagClientDto> = transaction {
        TagModel
            .select(TagModel.id, TagModel.name)
            .orderBy(TagModel.position)
            .where { TagModel.visible eq true }
            .map {
                TagClientDto(it[TagModel.id].value, it[TagModel.name])
            }
    }
}