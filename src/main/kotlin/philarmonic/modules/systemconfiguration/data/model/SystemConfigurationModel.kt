package philarmonic.modules.systemconfiguration.data.model


import org.jetbrains.exposed.sql.ReferenceOption
import philarmonic.utils.database.BaseIntIdTable

object SystemConfigurationModel: BaseIntIdTable() {
    
    val name = text("name")
    val email = text("email")
    val address = text("address")
    val city = text("city")
    val addressShort = text("addressShort")
    val vk = text("vk")
    val telegram = text("telegram")
    val soundCloud = text("soundCloud")
}