package philarmonic.modules.childrenprograms.service

import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel
import philarmonic.modules.childrenprograms.data.dao.ChildrenProgramsDao
import philarmonic.modules.childrenprograms.data.dto.*

class ChildrenProgramsService(di: DI) : KodeinService(di) {
    fun create(createDto: CreateChildrenProgramsDto): ChildrenProgramsDto = transaction {
        val created = ChildrenProgramsDao.new {
            this.name = createDto.name
            this.image = createDto.image
            this.description = createDto.description ?: ""
            this.visible = createDto.visible
            this.position = createDto.position
        }

        created.toOutputDto()
    }

    fun getAll(): List<ChildrenProgramsListDto> = transaction {
        ChildrenProgramsModel
            .selectAll()
            .orderBy(ChildrenProgramsModel.position)
            .map {
                ChildrenProgramsListDto(
                    it[ChildrenProgramsModel.id].value,
                    it[ChildrenProgramsModel.name],
                    it[ChildrenProgramsModel.visible],
                    it[ChildrenProgramsModel.position]
                )
            }
    }

    fun getOne(id: Int): ChildrenProgramsDto = transaction {
        ChildrenProgramsDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdateChildrenProgramsDto): ChildrenProgramsDto = transaction {
        val dao = ChildrenProgramsDao[id]
        dao.name = updateDto.name ?: dao.name
        dao.image = updateDto.image ?: dao.image
        dao.description = updateDto.description ?: dao.description
        dao.visible = updateDto.visible ?: dao.visible
        dao.position = updateDto.position ?: dao.position

        dao.flush()

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = ChildrenProgramsDao[id]

        dao.delete()

        true
    }

    fun toggleVisible(id: Int): ChildrenProgramsDto = transaction {
        val dao = ChildrenProgramsDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): ChildrenProgramsDto = transaction {
        val dao = ChildrenProgramsDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = ChildrenProgramsDao.find { ChildrenProgramsModel.position eq prevPosition }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = ChildrenProgramsDao.find { ChildrenProgramsModel.position eq prevPosition }.firstOrNull()
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

    fun positionDown(id: Int): ChildrenProgramsDto = transaction {
        val allCount = ChildrenProgramsDao.all().count()
        val dao = ChildrenProgramsDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = ChildrenProgramsDao.find { ChildrenProgramsModel.position eq prevPosition }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = ChildrenProgramsDao.find { ChildrenProgramsModel.position eq prevPosition }.firstOrNull()
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

    fun getAllClient(): List<ChildrenProgramClientDto> = transaction {
        ChildrenProgramsModel
            .select(ChildrenProgramsModel.name, ChildrenProgramsModel.description, ChildrenProgramsModel.image)
            .where { ChildrenProgramsModel.visible eq true }
            .orderBy(ChildrenProgramsModel.position)
            .map {
                ChildrenProgramClientDto(
                    it[ChildrenProgramsModel.name],
                    it[ChildrenProgramsModel.description],
                    it[ChildrenProgramsModel.image] ?: ""
                )
            }
    }
}