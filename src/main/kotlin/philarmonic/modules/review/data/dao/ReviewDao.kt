package philarmonic.modules.review.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.review.data.dto.ReviewDto
import philarmonic.modules.review.data.model.ReviewModel
import philarmonic.modules.eventsecond.data.dao.EventSecondDao

class ReviewDao(id: EntityID<Int>) : BaseIntEntity<ReviewDto>(id, ReviewModel) {
    companion object : BaseIntEntityClass<ReviewDto, ReviewDao>(ReviewModel)

    var date by ReviewModel.date
    var name by ReviewModel.name
    var review by ReviewModel.review
    var eventId by EventSecondDao optionalReferencedOn ReviewModel.eventId
    var eventIdId by ReviewModel.eventId
    var visible by ReviewModel.visible
    var position by ReviewModel.position

    override fun toOutputDto(): ReviewDto =
    ReviewDto(
        this.id.value,
        this.date,
        this.name,
        this.review,
        this.eventIdId?.value,
        this.visible,
        this.position,
    )
}