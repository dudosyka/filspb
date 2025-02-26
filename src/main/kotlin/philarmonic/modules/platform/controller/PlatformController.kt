package philarmonic.modules.platform.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.platform.data.dto.CreatePlatformDto
import philarmonic.modules.platform.data.dto.UpdatePlatformDto
import philarmonic.modules.platform.service.PlatformService

class PlatformController(override val di: DI) : KodeinController() {
    private val platformService: PlatformService by instance()

    override fun Route.registerRoutes() {
        route("/platform") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreatePlatformDto>()
                    call.respond(platformService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdatePlatformDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(platformService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(platformService.delete(id))
                }
                patch("/visible/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(platformService.toggleVisible(id))
                }
                patch("/position/{id}/up") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(platformService.positionUp(id))
                }
                patch("/position/{id}/down") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(platformService.positionDown(id))
                }
            }
            get {
                call.respond(platformService.getAll())
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(platformService.getOne(id))
            }
            route("client") {
                get {
                    call.respond(platformService.getAllClient())
                }
            }
        }
    }
}