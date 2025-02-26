package philarmonic.modules.systemconfiguration.service

import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import philarmonic.modules.systemconfiguration.data.dto.CreateSystemConfigurationDto
import philarmonic.modules.systemconfiguration.data.dto.UpdateSystemConfigurationDto
import philarmonic.modules.systemconfiguration.data.dto.SystemConfigurationDto
import philarmonic.modules.systemconfiguration.data.dao.SystemConfigurationDao

class SystemConfigurationService(di: DI) : KodeinService(di) {
    fun create(createDto: CreateSystemConfigurationDto): SystemConfigurationDto = transaction {
        val created = SystemConfigurationDao.new {
            this.name = createDto.name
            this.email = createDto.email
            this.address = createDto.address
            this.city = createDto.city
            this.addressShort = createDto.addressShort
            this.vk = createDto.vk
            this.telegram = createDto.telegram
            this.soundCloud = createDto.soundCloud
        }

        created.toOutputDto()
    }

    fun getOne(id: Int): SystemConfigurationDto = transaction {
        SystemConfigurationDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdateSystemConfigurationDto): SystemConfigurationDto = transaction {
        val dao = SystemConfigurationDao[id]
        dao.name = updateDto.name ?: dao.name
        dao.email = updateDto.email ?: dao.email
        dao.address = updateDto.address ?: dao.address
        dao.city = updateDto.city ?: dao.city
        dao.addressShort = updateDto.addressShort ?: dao.addressShort
        dao.vk = updateDto.vk ?: dao.vk
        dao.telegram = updateDto.telegram ?: dao.telegram
        dao.soundCloud = updateDto.soundCloud ?: dao.soundCloud

        dao.flush()

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = SystemConfigurationDao[id]

        dao.delete()

        true
    }
}