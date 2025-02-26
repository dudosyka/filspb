package philarmonic.modules.peoplecategory.service

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import philarmonic.exceptions.BadRequestException
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import philarmonic.modules.peoplecategory.data.dto.CreatePeopleCategoryDto
import philarmonic.modules.peoplecategory.data.dto.UpdatePeopleCategoryDto
import philarmonic.modules.peoplecategory.data.dto.PeopleCategoryDto
import philarmonic.modules.peoplecategory.data.dto.PeopleCategoryListDto
import philarmonic.modules.peoplecategory.data.model.PeopleCategoryModel
import philarmonic.modules.peoplecategory.data.dao.PeopleCategoryDao
import philarmonic.modules.platform.data.dao.PlatformDao
import philarmonic.modules.platform.data.model.PlatformModel

class PeopleCategoryService(di: DI) : KodeinService(di) {
    fun create(createDto: CreatePeopleCategoryDto): PeopleCategoryDto = transaction {
        val created = PeopleCategoryDao.new {
            this.name = createDto.name
            this.visible = createDto.visible
            this.position = createDto.position
        }

        created.toOutputDto()
    }

    fun getAll(): List<PeopleCategoryListDto> = transaction {
        PeopleCategoryModel
            .selectAll()
            .orderBy(PeopleCategoryModel.position)
            .map {
                PeopleCategoryListDto(
                    it[PeopleCategoryModel.id].value,
                    it[PeopleCategoryModel.name],
                    it[PeopleCategoryModel.visible],
                    it[PeopleCategoryModel.position]
                )
            }
    }

    fun getOne(id: Int): PeopleCategoryDto = transaction {
        PeopleCategoryDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdatePeopleCategoryDto): PeopleCategoryDto = transaction {
        val dao = PeopleCategoryDao[id]
        dao.name = updateDto.name ?: dao.name
        dao.visible = updateDto.visible ?: dao.visible
        dao.position = updateDto.position ?: dao.position

        dao.flush()

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = PeopleCategoryDao[id]

        dao.delete()

        true
    }

    fun toggleVisible(id: Int): PeopleCategoryDto = transaction {
        val dao = PeopleCategoryDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): PeopleCategoryDto = transaction {
        val dao = PeopleCategoryDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = PeopleCategoryDao.find { PeopleCategoryModel.position eq prevPosition }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = PeopleCategoryDao.find { PeopleCategoryModel.position eq prevPosition }.firstOrNull()
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

    fun positionDown(id: Int): PeopleCategoryDto = transaction {
        val allCount = PeopleCategoryDao.all().count()
        val dao = PeopleCategoryDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = PeopleCategoryDao.find { PeopleCategoryModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = PeopleCategoryDao.find { PeopleCategoryModel.position eq (dao.position + 1) }.firstOrNull()
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