package philarmonic.modules.review.service


import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import philarmonic.modules.review.data.dto.CreateReviewDto
import philarmonic.modules.review.data.dto.UpdateReviewDto
import philarmonic.modules.review.data.dto.ReviewDto
import philarmonic.modules.review.data.dto.ReviewListDto
import philarmonic.modules.review.data.model.ReviewModel
import philarmonic.modules.review.data.dao.ReviewDao
import philarmonic.modules.event.data.dao.EventDao
import philarmonic.modules.eventsecond.data.dao.EventSecondDao
import philarmonic.modules.eventsecond.data.model.EventSecondModel

class ReviewService(di: DI) : KodeinService(di) {
    fun create(createDto: CreateReviewDto): ReviewDto = transaction {
        val created = ReviewDao.new {
            this.date = createDto.date
            this.name = createDto.name ?: ""
            this.review = createDto.review
            this.eventId = EventSecondDao[createDto.eventId ?: 0]
            this.visible = createDto.visible
            this.position = createDto.position
        }

        created.toOutputDto()
    }

    fun getAll(): List<ReviewListDto> = transaction {
        ReviewModel
            .leftJoin(EventSecondModel)
            .selectAll()
            .orderBy(ReviewModel.position)
            .map {
                ReviewListDto(
                    it[ReviewModel.id].value,
                    it[ReviewModel.date] ?: 0L,
                    it[EventSecondModel.name],
                    it[ReviewModel.name],
                    it[ReviewModel.visible],
                    it[ReviewModel.position]
                )
            }
    }

    fun getOne(id: Int): ReviewDto = transaction {
        ReviewDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdateReviewDto): ReviewDto = transaction {
        val dao = ReviewDao[id]
        dao.date = updateDto.date ?: dao.date
        dao.name = updateDto.name ?: dao.name
        dao.review = updateDto.review ?: dao.review
        dao.eventId = if (updateDto.eventId == null) dao.eventId else EventSecondDao[updateDto.eventId]
        dao.visible = updateDto.visible ?: dao.visible
        dao.position = updateDto.position ?: dao.position

        dao.flush()

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = ReviewDao[id]

        dao.delete()

        true
    }

    fun toggleVisible(id: Int): ReviewDto = transaction {
        val dao = ReviewDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): ReviewDto = transaction {
        val dao = ReviewDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = ReviewDao.find { ReviewModel.position eq prevPosition }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = ReviewDao.find { ReviewModel.position eq prevPosition }.firstOrNull()
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

    fun positionDown(id: Int): ReviewDto = transaction {
        val allCount = ReviewDao.all().count()
        val dao = ReviewDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = ReviewDao.find { ReviewModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = ReviewDao.find { ReviewModel.position eq (dao.position + 1) }.firstOrNull()
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