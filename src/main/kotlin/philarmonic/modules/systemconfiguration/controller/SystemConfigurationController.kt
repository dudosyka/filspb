package philarmonic.modules.systemconfiguration.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.systemconfiguration.data.dto.CreateSystemConfigurationDto
import philarmonic.modules.systemconfiguration.data.dto.UpdateSystemConfigurationDto
import philarmonic.modules.systemconfiguration.service.SystemConfigurationService

class SystemConfigurationController(override val di: DI) : KodeinController() {
    private val systemConfigurationService: SystemConfigurationService by instance()

    override fun Route.registerRoutes() {
        route("/system") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreateSystemConfigurationDto>()
                    call.respond(systemConfigurationService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdateSystemConfigurationDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(systemConfigurationService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(systemConfigurationService.delete(id))
                }
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(systemConfigurationService.getOne(id))
            }
            route("client") {
                get("1") {
                    call.respond(systemConfigurationService.getOne(1))
                }
            }
        }
    }
}