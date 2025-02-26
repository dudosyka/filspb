package philarmonic.modules.tag.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.tag.data.dto.TagDto
import philarmonic.modules.tag.data.model.TagModel


class TagDao(id: EntityID<Int>) : BaseIntEntity<TagDto>(id, TagModel) {
    companion object : BaseIntEntityClass<TagDto, TagDao>(TagModel)

    var name by TagModel.name
    var visible by TagModel.visible
    var position by TagModel.position

    override fun toOutputDto(): TagDto =
    TagDto(
        this.id.value,
        this.name,
        this.visible,
        this.position,
    )
}