package philarmonic.modules.hall.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.hall.data.dto.HallDto
import philarmonic.modules.hall.data.model.HallModel
import philarmonic.modules.platform.data.dao.PlatformDao

class HallDao(id: EntityID<Int>) : BaseIntEntity<HallDto>(id, HallModel) {
    companion object : BaseIntEntityClass<HallDto, HallDao>(HallModel)

    var name by HallModel.name
    var visible by HallModel.visible
    var position by HallModel.position

    override fun toOutputDto(): HallDto =
    HallDto(
        this.id.value,
        this.name,
        this.visible,
        this.position,
    )
}