package philarmonic.modules.slider.service

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import philarmonic.exceptions.BadRequestException
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.childrenprograms.data.dao.ChildrenProgramsDao
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel
import philarmonic.modules.files.FilesService
import philarmonic.modules.slider.data.dao.SliderDao
import philarmonic.modules.slider.data.dto.*
import philarmonic.modules.slider.data.model.SliderModel

class SliderService(di: DI) : KodeinService(di) {
    fun create(createDto: CreateSliderDto): SliderDto = transaction {
        val created = SliderDao.new {
            this.desktopImage = createDto.desktopImage ?: ""
            this.mobileImage = createDto.mobileImage ?: ""
            this.link = createDto.link ?: ""
            this.buttonText = createDto.buttonText ?: ""
            this.visible = createDto.visible
            this.position = createDto.position
        }

        created.toOutputDto()
    }

    fun getAll(): List<SliderListDto> = transaction {
        SliderModel
            .selectAll()
            .orderBy(SliderModel.position)
            .map {
                SliderListDto(
                    it[SliderModel.id].value,
                    it[SliderModel.desktopImage] ?: "",
                    it[SliderModel.buttonText] ?: "",
                    it[SliderModel.visible],
                    it[SliderModel.position]
                )
            }
    }

    fun getOne(id: Int): SliderDto = transaction {
        SliderDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdateSliderDto): SliderDto = transaction {
        val dao = SliderDao[id]

        dao.desktopImage = updateDto.desktopImage ?: dao.desktopImage
        dao.mobileImage = updateDto.mobileImage ?: dao.mobileImage
        dao.link = updateDto.link ?: dao.link
        dao.buttonText = updateDto.buttonText ?: dao.buttonText
        dao.visible = updateDto.visible ?: dao.visible
        dao.position = updateDto.position ?: dao.position

        dao.flush()

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = SliderDao[id]

        dao.delete()

        true
    }

    fun toggleVisible(id: Int): SliderDto = transaction {
        val dao = SliderDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): SliderDto = transaction {
        val dao = SliderDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = SliderDao.find { SliderModel.position eq prevPosition }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = SliderDao.find { SliderModel.position eq prevPosition }.firstOrNull()
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

    fun positionDown(id: Int): SliderDto = transaction {
        val allCount = SliderDao.all().count()
        val dao = SliderDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = SliderDao.find { SliderModel.position eq prevPosition }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = SliderDao.find { SliderModel.position eq prevPosition }.firstOrNull()
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

    fun getAllClient(): List<SliderClientDto> = transaction {
        SliderModel
            .select(SliderModel.desktopImage, SliderModel.mobileImage, SliderModel.link, SliderModel.buttonText)
            .where { SliderModel.visible eq true }
            .map {
                SliderClientDto(
                    it[SliderModel.desktopImage] ?: "",
                    it[SliderModel.mobileImage] ?: "",
                    it[SliderModel.link] ?: "",
                    it[SliderModel.buttonText] ?: "",
                )
            }
    }
}