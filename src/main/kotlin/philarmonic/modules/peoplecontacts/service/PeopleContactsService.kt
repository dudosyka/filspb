package philarmonic.modules.peoplecontacts.service

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import philarmonic.exceptions.BadRequestException
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import philarmonic.modules.peoplecontacts.data.model.PeopleContactsModel
import philarmonic.modules.peoplecontacts.data.dao.PeopleContactsDao
import philarmonic.modules.peoplecontacts.data.dto.*
import philarmonic.modules.platform.data.dao.PlatformDao
import philarmonic.modules.platform.data.model.PlatformModel

class PeopleContactsService(di: DI) : KodeinService(di) {
    fun create(createDto: CreatePeopleContactsDto): PeopleContactsDto = transaction {
        val created = PeopleContactsDao.new {
            this.surname = createDto.surname ?: ""
            this.io = createDto.io ?: ""
            this.email = createDto.email ?: ""
            this.photo = createDto.photo ?: ""
            this.workPosition = createDto.workPosition ?: ""
            this.visible = createDto.visible
            this.position = createDto.position
        }

        created.toOutputDto()
    }

    fun getAll(): List<PeopleContactsListDto> = transaction {
        PeopleContactsModel
            .selectAll()
            .orderBy(PeopleContactsModel.position)
            .map {
                PeopleContactsListDto(
                    it[PeopleContactsModel.id].value,
                    it[PeopleContactsModel.surname] ?: "",
                    it[PeopleContactsModel.workPosition] ?: "",
                    it[PeopleContactsModel.visible],
                    it[PeopleContactsModel.position]
                )
            }
    }

    fun getOne(id: Int): PeopleContactsDto = transaction {
        PeopleContactsDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdatePeopleContactsDto): PeopleContactsDto = transaction {
        val dao = PeopleContactsDao[id]
        dao.surname = updateDto.surname ?: dao.surname
        dao.io = updateDto.io ?: dao.io
        dao.email = updateDto.email ?: dao.email
        dao.photo = updateDto.photo ?: dao.photo
        dao.workPosition = updateDto.workPosition ?: dao.workPosition
        dao.visible = updateDto.visible ?: dao.visible
        dao.position = updateDto.position ?: dao.position

        dao.flush()

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = PeopleContactsDao[id]

        dao.delete()

        true
    }

    fun toggleVisible(id: Int): PeopleContactsDto = transaction {
        val dao = PeopleContactsDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): PeopleContactsDto = transaction {
        val dao = PeopleContactsDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = PeopleContactsDao.find { PeopleContactsModel.position eq prevPosition }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = PeopleContactsDao.find { PeopleContactsModel.position eq prevPosition }.firstOrNull()
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

    fun positionDown(id: Int): PeopleContactsDto = transaction {
        val allCount = PeopleContactsDao.all().count()
        val dao = PeopleContactsDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = PeopleContactsDao.find { PeopleContactsModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = PeopleContactsDao.find { PeopleContactsModel.position eq (dao.position + 1) }.firstOrNull()
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

    fun getAllClient(): List<PeopleContactsClientDto> = transaction {
        PeopleContactsModel
            .select(PeopleContactsModel.surname, PeopleContactsModel.workPosition, PeopleContactsModel.email, PeopleContactsModel.photo)
            .orderBy(PeopleContactsModel.position)
            .where { PeopleContactsModel.visible eq true }
            .map {
                PeopleContactsClientDto(
                    it[PeopleContactsModel.surname] ?: "",
                    it[PeopleContactsModel.workPosition] ?: "",
                    it[PeopleContactsModel.photo] ?: "",
                    it[PeopleContactsModel.email] ?: "",
                )
            }
    }
}