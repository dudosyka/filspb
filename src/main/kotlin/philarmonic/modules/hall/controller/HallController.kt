package philarmonic.modules.hall.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.hall.data.dto.CreateHallDto
import philarmonic.modules.hall.data.dto.UpdateHallDto
import philarmonic.modules.hall.service.HallService

class HallController(override val di: DI) : KodeinController() {
    private val hallService: HallService by instance()

    override fun Route.registerRoutes() {
        route("/hall") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreateHallDto>()
                    call.respond(hallService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdateHallDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(hallService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(hallService.delete(id))
                }
                patch("/visible/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(hallService.toggleVisible(id))
                }
                patch("/position/{id}/up") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(hallService.positionUp(id))
                }
                patch("/position/{id}/down") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(hallService.positionDown(id))
                }
            }
            get {
                call.respond(hallService.getAll())
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(hallService.getOne(id))
            }
        }
    }
}