package philarmonic.modules.news.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.news.data.dto.NewsDto
import philarmonic.modules.news.data.model.NewsModel


class NewsDao(id: EntityID<Int>) : BaseIntEntity<NewsDto>(id, NewsModel) {
    companion object : BaseIntEntityClass<NewsDto, NewsDao>(NewsModel)

    var date by NewsModel.date
    var name by NewsModel.name
    var image by NewsModel.image
    var shortDescription by NewsModel.shortDescription
    var description by NewsModel.description
    var visible by NewsModel.visible
    var position by NewsModel.position

    override fun toOutputDto(): NewsDto =
    NewsDto(
        this.id.value,
        this.date,
        this.name,
        this.image,
        this.shortDescription,
        this.description,
        this.visible,
        this.position,
    )
}