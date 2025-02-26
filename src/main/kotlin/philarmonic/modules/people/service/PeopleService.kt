package philarmonic.modules.people.service

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import philarmonic.exceptions.BadRequestException
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import philarmonic.modules.people.data.model.PeopleModel
import philarmonic.modules.people.data.dao.PeopleDao
import philarmonic.modules.people.data.dto.*
import philarmonic.modules.peoplecategory.data.dao.PeopleCategoryDao
import philarmonic.modules.peoplecategory.data.model.PeopleCategoryModel
import philarmonic.modules.platform.data.dao.PlatformDao
import philarmonic.modules.platform.data.model.PlatformModel
import philarmonic.utils.database.idValue

class PeopleService(di: DI) : KodeinService(di) {
    fun create(createDto: CreatePeopleDto): PeopleDto = transaction {
        val created = PeopleDao.new {
            this.name = createDto.name ?: ""
            this.photo = createDto.photo
            this.workPosition = createDto.workPosition
            this.category = PeopleCategoryDao[createDto.category ?: 0]
            this.visible = createDto.visible
            this.position = createDto.position
        }

        created.toOutputDto()
    }

    fun getAll(): List<PeopleListDto> = transaction {
        PeopleModel
            .selectAll()
            .orderBy(PeopleModel.position)
            .map {
                PeopleListDto(
                    it[PeopleModel.id].value,
                    it[PeopleModel.name],
                    it[PeopleModel.visible],
                    it[PeopleModel.position]
                )
            }
    }

    fun getOne(id: Int): PeopleDto = transaction {
        PeopleDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdatePeopleDto): PeopleDto = transaction {
        val dao = PeopleDao[id]
        dao.name = updateDto.name ?: dao.name
        dao.photo = updateDto.photo ?: dao.photo
        dao.workPosition = updateDto.workPosition ?: dao.workPosition
        dao.category = if (updateDto.category == null) dao.category else PeopleCategoryDao[updateDto.category]
        dao.visible = updateDto.visible ?: dao.visible
        dao.position = updateDto.position ?: dao.position

        dao.flush()

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = PeopleDao[id]

        dao.delete()

        true
    }

    fun toggleVisible(id: Int): PeopleDto = transaction {
        val dao = PeopleDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): PeopleDto = transaction {
        val dao = PeopleDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = PeopleDao.find { PeopleModel.position eq prevPosition }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = PeopleDao.find { PeopleModel.position eq prevPosition }.firstOrNull()
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

    fun positionDown(id: Int): PeopleDto = transaction {
        val allCount = PeopleDao.all().count()
        val dao = PeopleDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = PeopleDao.find { PeopleModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = PeopleDao.find { PeopleModel.position eq (dao.position + 1) }.firstOrNull()
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

    fun getAllClient(): List<PeopleByCategoryDto> = transaction {
        PeopleCategoryModel
            .select(PeopleCategoryModel.name, PeopleCategoryModel.id)
            .where { PeopleCategoryModel.visible eq true }
            .orderBy(PeopleCategoryModel.position)
            .map {
                PeopleByCategoryDto(
                    it[PeopleCategoryModel.name],
                    PeopleModel
                        .select(PeopleModel.name, PeopleModel.workPosition, PeopleModel.photo)
                        .orderBy(PeopleModel.position)
                        .where { (PeopleModel.category eq it[PeopleCategoryModel.id].value) and (PeopleModel.visible eq true)}
                        .map { person ->
                            PersonClientDto(
                                person[PeopleModel.name],
                                person[PeopleModel.workPosition] ?: "",
                                person[PeopleModel.photo]
                            )
                        }
                )
            }
    }
}