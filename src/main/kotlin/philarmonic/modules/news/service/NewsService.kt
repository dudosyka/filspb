package philarmonic.modules.news.service

import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import philarmonic.exceptions.BadRequestException
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import philarmonic.modules.news.data.model.NewsModel
import philarmonic.modules.news.data.dao.NewsDao
import philarmonic.modules.news.data.dto.*
import philarmonic.modules.platform.data.dao.PlatformDao
import philarmonic.modules.platform.data.model.PlatformModel
import philarmonic.utils.files.FilesUtil

class NewsService(di: DI) : KodeinService(di) {
    fun create(createDto: CreateNewsDto): NewsDto = transaction {
        val imageName = FilesUtil.buildName(createDto.imageName ?: throw BadRequestException("Bad filename"), "news")

        val created = NewsDao.new {
            this.date = createDto.date ?: 0L
            this.name = createDto.name
            this.image = imageName
            this.shortDescription = createDto.shortDescription ?: ""
            this.description = createDto.description ?: ""
            this.visible = createDto.visible
            this.position = createDto.position
        }

        FilesUtil.upload(with(createDto.image) {
            if (this == null)
                throw BadRequestException("Bad file")
            this.split("base64,")[1]
        } , imageName)

        created.toOutputDto()
    }

    fun getAll(): List<NewsListDto> = transaction {
        NewsModel
            .selectAll()
            .orderBy(NewsModel.position)
            .map {
                NewsListDto(
                    it[NewsModel.id].value,
                    it[NewsModel.date] ?: 0L,
                    it[NewsModel.name],
                    it[NewsModel.visible],
                    it[NewsModel.position]
                )
            }
    }

    fun getOne(id: Int): NewsDto = transaction {
        NewsDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdateNewsDto): NewsDto = transaction {
        val dao = NewsDao[id]
        dao.date = updateDto.date ?: dao.date
        dao.name = updateDto.name ?: dao.name
        val oldImageName = if (updateDto.image != null && updateDto.imageName != null) {
            val fileName = FilesUtil.buildName(updateDto.imageName, "news")
            val old = dao.image
            dao.image = fileName
            FilesUtil.upload(updateDto.image.split("base64,")[1], fileName)
            old
        } else null
        dao.shortDescription = updateDto.shortDescription ?: dao.shortDescription
        dao.description = updateDto.description ?: dao.description
        dao.visible = updateDto.visible ?: dao.visible
        dao.position = updateDto.position ?: dao.position

        dao.flush()

        if (updateDto.image != null && updateDto.imageName != null && oldImageName != null) {
            FilesUtil.removeFile(oldImageName)
        }

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = NewsDao[id]

        dao.delete()

        if (dao.image != null)
            FilesUtil.removeFile(dao.image!!)

        true
    }

    fun toggleVisible(id: Int): NewsDto = transaction {
        val dao = NewsDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): NewsDto = transaction {
        val dao = NewsDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = NewsDao.find { NewsModel.position eq prevPosition }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = NewsDao.find { NewsModel.position eq prevPosition }.firstOrNull()
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

    fun positionDown(id: Int): NewsDto = transaction {
        val allCount = NewsDao.all().count()
        val dao = NewsDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = NewsDao.find { NewsModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = NewsDao.find { NewsModel.position eq (dao.position + 1) }.firstOrNull()
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

    fun getOneClient(id: Int): NewsClientDto = transaction {
        NewsModel
            .select(NewsModel.id, NewsModel.name, NewsModel.image, NewsModel.name, NewsModel.description)
            .where {
                NewsModel.id eq id
            }
            .limit(1)
            .map {
                NewsClientDto(it[NewsModel.id].value, it[NewsModel.name], it[NewsModel.image] ?: "", it[NewsModel.description] ?: "")
            }
            .firstOrNull() ?: throw NotFoundException("Article not found")
    }

    fun getAllClient(count: Int): List<NewsListClientDto> = transaction {
        with(NewsModel
            .select(NewsModel.id, NewsModel.date, NewsModel.name, NewsModel.shortDescription, NewsModel.image)
            .where { NewsModel.visible eq true }
            .orderBy(NewsModel.date, SortOrder.DESC)) {
            if (count == 0) this
            else limit(count)
        }.map {
                NewsListClientDto(
                    it[NewsModel.id].value,
                    it[NewsModel.date] ?: 0L,
                    it[NewsModel.name],
                    it[NewsModel.shortDescription] ?: "",
                    it[NewsModel.image] ?: ""
                )
            }

    }
}