package philarmonic.modules.platform.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.platform.data.dto.PlatformDto
import philarmonic.modules.platform.data.model.PlatformModel


class PlatformDao(id: EntityID<Int>) : BaseIntEntity<PlatformDto>(id, PlatformModel) {
    companion object : BaseIntEntityClass<PlatformDto, PlatformDao>(PlatformModel)

    var name by PlatformModel.name
    var address by PlatformModel.address
    var visible by PlatformModel.visible
    var position by PlatformModel.position

    override fun toOutputDto(): PlatformDto =
    PlatformDto(
        this.id.value,
        this.name,
        this.address,
        this.visible,
        this.position,
    )
}