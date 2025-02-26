package philarmonic.modules.partners.service

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import philarmonic.exceptions.BadRequestException
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import philarmonic.modules.partners.data.model.PartnersModel
import philarmonic.modules.partners.data.dao.PartnersDao
import philarmonic.modules.partners.data.dto.*
import philarmonic.modules.platform.data.dao.PlatformDao
import philarmonic.modules.platform.data.model.PlatformModel

class PartnersService(di: DI) : KodeinService(di) {
    fun create(createDto: CreatePartnersDto): PartnersDto = transaction {
        val created = PartnersDao.new {
            this.name = createDto.name
            this.image = createDto.image ?: ""
            this.visible = createDto.visible
            this.position = createDto.position
        }

        created.toOutputDto()
    }

    fun getAll(): List<PartnersListDto> = transaction {
        PartnersModel
            .selectAll()
            .orderBy(PartnersModel.position)
            .map {
                PartnersListDto(
                    it[PartnersModel.id].value,
                    it[PartnersModel.name],
                    it[PartnersModel.image] ?: "",
                    it[PartnersModel.visible],
                    it[PartnersModel.position]
                )
            }
    }

    fun getOne(id: Int): PartnersDto = transaction {
        PartnersDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdatePartnersDto): PartnersDto = transaction {
        val dao = PartnersDao[id]
        dao.name = updateDto.name ?: dao.name
        dao.image = updateDto.image ?: dao.image
        dao.visible = updateDto.visible ?: dao.visible
        dao.position = updateDto.position ?: dao.position

        dao.flush()

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = PartnersDao[id]

        dao.delete()

        true
    }

    fun toggleVisible(id: Int): PartnersDto = transaction {
        val dao = PartnersDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): PartnersDto = transaction {
        val dao = PartnersDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = PartnersDao.find { PartnersModel.position eq prevPosition }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = PartnersDao.find { PartnersModel.position eq prevPosition }.firstOrNull()
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

    fun positionDown(id: Int): PartnersDto = transaction {
        val allCount = PartnersDao.all().count()
        val dao = PartnersDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = PartnersDao.find { PartnersModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = PartnersDao.find { PartnersModel.position eq (dao.position + 1) }.firstOrNull()
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

    fun getAllClient(): List<PartnersClientDto> = transaction {
        PartnersModel
            .select(PartnersModel.image)
            .orderBy(PartnersModel.position)
            .where { PartnersModel.visible eq true }
            .map { PartnersClientDto(it[PartnersModel.image] ?: "") }
    }
}