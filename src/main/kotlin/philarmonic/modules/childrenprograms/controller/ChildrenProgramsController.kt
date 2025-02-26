package philarmonic.modules.childrenprograms.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.childrenprograms.data.dto.CreateChildrenProgramsDto
import philarmonic.modules.childrenprograms.data.dto.UpdateChildrenProgramsDto
import philarmonic.modules.childrenprograms.service.ChildrenProgramsService

class ChildrenProgramsController(override val di: DI) : KodeinController() {
    private val childrenProgramsService: ChildrenProgramsService by instance()

    override fun Route.registerRoutes() {
        route("/children/programs") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreateChildrenProgramsDto>()
                    call.respond(childrenProgramsService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdateChildrenProgramsDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(childrenProgramsService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(childrenProgramsService.delete(id))
                }
                patch("/visible/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(childrenProgramsService.toggleVisible(id))
                }
                patch("/position/{id}/up") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(childrenProgramsService.positionUp(id))
                }
                patch("/position/{id}/down") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(childrenProgramsService.positionDown(id))
                }
            }
            get {
                call.respond(childrenProgramsService.getAll())
            }
            route("client") {
                get {
                    call.respond(childrenProgramsService.getAllClient())
                }
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(childrenProgramsService.getOne(id))
            }
        }
    }
}