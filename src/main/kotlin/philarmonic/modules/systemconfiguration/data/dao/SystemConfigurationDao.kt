package philarmonic.modules.systemconfiguration.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.systemconfiguration.data.dto.SystemConfigurationDto
import philarmonic.modules.systemconfiguration.data.model.SystemConfigurationModel


class SystemConfigurationDao(id: EntityID<Int>) : BaseIntEntity<SystemConfigurationDto>(id, SystemConfigurationModel) {
    companion object : BaseIntEntityClass<SystemConfigurationDto, SystemConfigurationDao>(SystemConfigurationModel)

    var name by SystemConfigurationModel.name
    var email by SystemConfigurationModel.email
    var address by SystemConfigurationModel.address
    var city by SystemConfigurationModel.city
    var addressShort by SystemConfigurationModel.addressShort
    var vk by SystemConfigurationModel.vk
    var telegram by SystemConfigurationModel.telegram
    var soundCloud by SystemConfigurationModel.soundCloud

    override fun toOutputDto(): SystemConfigurationDto =
    SystemConfigurationDto(
        this.id.value,
        this.name,
        this.email,
        this.address,
        this.city,
        this.addressShort,
        this.vk,
        this.telegram,
        this.soundCloud,
    )
}