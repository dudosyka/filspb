package philarmonic.modules.departmentscontacts.service

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.time
import philarmonic.exceptions.BadRequestException
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import philarmonic.modules.departmentscontacts.data.model.DepartmentsContactsModel
import philarmonic.modules.departmentscontacts.data.dao.DepartmentsContactsDao
import philarmonic.modules.departmentscontacts.data.dto.*
import philarmonic.modules.platform.data.dao.PlatformDao
import philarmonic.modules.platform.data.model.PlatformModel

class DepartmentsContactsService(di: DI) : KodeinService(di) {
    fun create(createDto: CreateDepartmentsContactsDto): DepartmentsContactsDto = transaction {
        val created = DepartmentsContactsDao.new {
            this.name = createDto.name
            this.phone = createDto.phone ?: ""
            this.timeTable = createDto.timeTable ?: ""
            this.visible = createDto.visible
            this.position = createDto.position
        }

        created.toOutputDto()
    }

    fun getAll(): List<DepartmentsContactsListDto> = transaction {
        DepartmentsContactsModel
            .selectAll()
            .orderBy(DepartmentsContactsModel.position)
            .map {
                DepartmentsContactsListDto(
                    it[DepartmentsContactsModel.id].value,
                    it[DepartmentsContactsModel.name],
                    it[DepartmentsContactsModel.visible],
                    it[DepartmentsContactsModel.position]
                )
            }
    }

    fun getOne(id: Int): DepartmentsContactsDto = transaction {
        DepartmentsContactsDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdateDepartmentsContactsDto): DepartmentsContactsDto = transaction {
        val dao = DepartmentsContactsDao[id]
        dao.name = updateDto.name ?: dao.name
        dao.phone = updateDto.phone ?: dao.phone
        dao.timeTable = updateDto.timeTable ?: dao.timeTable
        dao.visible = updateDto.visible ?: dao.visible
        dao.position = updateDto.position ?: dao.position

        dao.flush()

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = DepartmentsContactsDao[id]

        dao.delete()

        true
    }

    fun toggleVisible(id: Int): DepartmentsContactsDto = transaction {
        val dao = DepartmentsContactsDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): DepartmentsContactsDto = transaction {
        val dao = DepartmentsContactsDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = DepartmentsContactsDao.find { DepartmentsContactsModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = DepartmentsContactsDao.find { DepartmentsContactsModel.position eq (dao.position + 1) }.firstOrNull()
            prevPosition += 1
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

    fun positionDown(id: Int): DepartmentsContactsDto = transaction {
        val allCount = DepartmentsContactsDao.all().count()
        val dao = DepartmentsContactsDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = DepartmentsContactsDao.find { DepartmentsContactsModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = DepartmentsContactsDao.find { DepartmentsContactsModel.position eq (dao.position + 1) }.firstOrNull()
            prevPosition += 1
        }

        if (bottom == null) {
            dao.position = (allCount - 1).toInt()
        } else {
            val topPos = bottom.position
            bottom.position = dao.position
            dao.position = topPos

            bottom.flush()
        }
        dao.flush()

        dao.toOutputDto()
    }

    fun getAllClient(): List<DepartmentContactsClientDto> = transaction {
        DepartmentsContactsModel
            .select(DepartmentsContactsModel.name, DepartmentsContactsModel.phone, DepartmentsContactsModel.timeTable)
            .orderBy(DepartmentsContactsModel.position)
            .where { DepartmentsContactsModel.visible eq true }
            .map {
                DepartmentContactsClientDto(
                    it[DepartmentsContactsModel.name],
                    it[DepartmentsContactsModel.phone] ?: "",
                    it[DepartmentsContactsModel.timeTable] ?: ""
                )
            }
    }
}