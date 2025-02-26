package philarmonic.modules.vacancy.service

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import philarmonic.exceptions.BadRequestException
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import philarmonic.modules.platform.data.dao.PlatformDao
import philarmonic.modules.platform.data.model.PlatformModel
import philarmonic.modules.vacancy.data.model.VacancyModel
import philarmonic.modules.vacancy.data.dao.VacancyDao
import philarmonic.modules.vacancy.data.dto.*

class VacancyService(di: DI) : KodeinService(di) {
    fun create(createDto: CreateVacancyDto): VacancyDto = transaction {
        val created = VacancyDao.new {
            this.name = createDto.name
            this.image = createDto.image ?: ""
            this.description = createDto.description ?: ""
            this.visible = createDto.visible
            this.position = createDto.position
        }

        created.toOutputDto()
    }

    fun getAll(): List<VacancyListDto> = transaction {
        VacancyModel
            .selectAll()
            .orderBy(VacancyModel.position)
            .map {
                VacancyListDto(
                    it[VacancyModel.id].value,
                    it[VacancyModel.name],
                    it[VacancyModel.visible],
                    it[VacancyModel.position]
                )
            }
    }

    fun getOne(id: Int): VacancyDto = transaction {
        VacancyDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdateVacancyDto): VacancyDto = transaction {
        val dao = VacancyDao[id]
        dao.name = updateDto.name ?: dao.name
        dao.image = updateDto.image ?: dao.image
        dao.description = updateDto.description ?: dao.description
        dao.visible = updateDto.visible ?: dao.visible
        dao.position = updateDto.position ?: dao.position

        dao.flush()

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = VacancyDao[id]

        dao.delete()

        true
    }

    fun toggleVisible(id: Int): VacancyDto = transaction {
        val dao = VacancyDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): VacancyDto = transaction {
        val dao = VacancyDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = VacancyDao.find { VacancyModel.position eq prevPosition }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = VacancyDao.find { VacancyModel.position eq prevPosition }.firstOrNull()
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

    fun positionDown(id: Int): VacancyDto = transaction {
        val allCount = VacancyDao.all().count()
        val dao = VacancyDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = VacancyDao.find { VacancyModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = VacancyDao.find { VacancyModel.position eq (dao.position + 1) }.firstOrNull()
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

    fun getAllClient(): List<VacancyClientDto> = transaction {
        VacancyModel
            .select(VacancyModel.name, VacancyModel.description, VacancyModel.image)
            .orderBy(VacancyModel.position)
            .where { VacancyModel.visible eq true }
            .map {
                VacancyClientDto(
                    it[VacancyModel.name],
                    it[VacancyModel.description] ?: "",
                    it[VacancyModel.image] ?: ""
                )
            }
    }
}