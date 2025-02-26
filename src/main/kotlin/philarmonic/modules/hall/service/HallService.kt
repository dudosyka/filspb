package philarmonic.modules.hall.service

import philarmonic.exceptions.BadRequestException
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import philarmonic.modules.hall.data.dto.CreateHallDto
import philarmonic.modules.hall.data.dto.UpdateHallDto
import philarmonic.modules.hall.data.dto.HallDto
import philarmonic.modules.hall.data.dto.HallListDto
import philarmonic.modules.hall.data.model.HallModel
import philarmonic.modules.hall.data.dao.HallDao
import philarmonic.modules.platform.data.model.PlatformModel
import philarmonic.modules.platform.data.dao.PlatformDao

class HallService(di: DI) : KodeinService(di) {
    fun create(createDto: CreateHallDto): HallDto = transaction {
        val created = HallDao.new {
            this.name = createDto.name
            this.visible = createDto.visible
            this.position = createDto.position
        }

        created.toOutputDto()
    }

    fun getAll(): List<HallListDto> = transaction {
        HallModel
            .selectAll()
            .orderBy(HallModel.position)
            .map {
                HallListDto(
                    it[HallModel.id].value,
                    it[HallModel.name],
                    it[HallModel.visible],
                    it[HallModel.position]
                )
            }
    }

    fun getOne(id: Int): HallDto = transaction {
        HallDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdateHallDto): HallDto = transaction {
        val dao = HallDao[id]
        dao.name = updateDto.name ?: dao.name
        dao.visible = updateDto.visible ?: dao.visible
        dao.position = updateDto.position ?: dao.position

        dao.flush()

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = HallDao[id]

        dao.delete()

        true
    }

    fun toggleVisible(id: Int): HallDto = transaction {
        val dao = HallDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): HallDto = transaction {
        val dao = HallDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = HallDao.find { HallModel.position eq prevPosition }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = HallDao.find { HallModel.position eq prevPosition }.firstOrNull()
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

    fun positionDown(id: Int): HallDto = transaction {
        val allCount = HallDao.all().count()
        val dao = HallDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = HallDao.find { HallModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = HallDao.find { HallModel.position eq (dao.position + 1) }.firstOrNull()
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
}