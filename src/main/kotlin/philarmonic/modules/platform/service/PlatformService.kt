package philarmonic.modules.platform.service

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import philarmonic.exceptions.BadRequestException
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import philarmonic.modules.platform.data.model.PlatformModel
import philarmonic.modules.platform.data.dao.PlatformDao
import philarmonic.modules.platform.data.dto.*
import philarmonic.modules.vacancy.data.model.VacancyModel

class PlatformService(di: DI) : KodeinService(di) {
    fun create(createDto: CreatePlatformDto): PlatformDto = transaction {
        val created = PlatformDao.new {
            this.name = createDto.name
            this.address = createDto.address ?: ""
            this.visible = createDto.visible
            this.position = createDto.position
        }

        created.toOutputDto()
    }

    fun getAll(): List<PlatformListDto> = transaction {
        PlatformModel
            .selectAll()
            .orderBy(PlatformModel.position)
            .map {
                PlatformListDto(
                    it[PlatformModel.id].value,
                    it[PlatformModel.name],
                    it[PlatformModel.address] ?: "",
                    it[PlatformModel.visible],
                    it[PlatformModel.position]
                )
            }
    }

    fun getOne(id: Int): PlatformDto = transaction {
        PlatformDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdatePlatformDto): PlatformDto = transaction {
        val dao = PlatformDao[id]
        dao.name = updateDto.name ?: dao.name
        dao.address = updateDto.address ?: dao.address
        dao.visible = updateDto.visible ?: dao.visible
        dao.position = updateDto.position ?: dao.position

        dao.flush()

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = PlatformDao[id]

        dao.delete()

        true
    }

    fun toggleVisible(id: Int): PlatformDto = transaction {
        val dao = PlatformDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): PlatformDto = transaction {
        val dao = PlatformDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = PlatformDao.find { PlatformModel.position eq prevPosition }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = PlatformDao.find { PlatformModel.position eq prevPosition }.firstOrNull()
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

    fun positionDown(id: Int): PlatformDto = transaction {
        val allCount = PlatformDao.all().count()
        val dao = PlatformDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = PlatformDao.find { PlatformModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = PlatformDao.find { PlatformModel.position eq (dao.position + 1) }.firstOrNull()
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

    fun getAllClient(): List<PlatformClientDto> = transaction {
        PlatformModel
            .select(PlatformModel.id, PlatformModel.name)
            .orderBy(PlatformModel.position)
            .where { PlatformModel.visible eq true }
            .map {
                PlatformClientDto(it[PlatformModel.id].value, it[PlatformModel.name])
            }
    }
}