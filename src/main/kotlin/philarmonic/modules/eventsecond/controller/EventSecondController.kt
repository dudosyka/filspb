package philarmonic.modules.eventsecond.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.eventsecond.data.dto.CreateEventSecondDto
import philarmonic.modules.eventsecond.data.dto.UpdateEventSecondDto
import philarmonic.modules.eventsecond.service.EventSecondService

class EventSecondController(override val di: DI) : KodeinController() {
    private val eventSecondService: EventSecondService by instance()

    override fun Route.registerRoutes() {
        route("/event/but/not/event") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreateEventSecondDto>()
                    call.respond(eventSecondService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdateEventSecondDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(eventSecondService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(eventSecondService.delete(id))
                }
                patch("/visible/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(eventSecondService.toggleVisible(id))
                }
                patch("/position/{id}/up") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(eventSecondService.positionUp(id))
                }
                patch("/position/{id}/down") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(eventSecondService.positionDown(id))
                }
            }
            get {
                call.respond(eventSecondService.getAll())
            }
            route("client") {
                get("repertoire") {
                    call.respond(eventSecondService.getRepertoire())
                }
                patch("update_base_64") {
                    call.respond(eventSecondService.updateOldBase64())
                }
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(eventSecondService.getOne(id))
            }
        }
    }
}