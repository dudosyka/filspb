package philarmonic.modules.event.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.event.data.dto.CreateEventDto
import philarmonic.modules.event.data.dto.UpdateEventDto
import philarmonic.modules.event.service.EventService

class EventController(override val di: DI) : KodeinController() {
    private val eventService: EventService by instance()

    override fun Route.registerRoutes() {
        route("/event") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreateEventDto>()
                    call.respond(eventService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdateEventDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(eventService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(eventService.delete(id))
                }
                patch("/visible/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(eventService.toggleVisible(id))
                }
                patch("/position/{id}/up") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(eventService.positionUp(id))
                }
                patch("/position/{id}/down") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(eventService.positionDown(id))
                }
            }
            get {
                call.respond(eventService.getAll())
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(eventService.getOne(id))
            }
            route("client") {
                get("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(eventService.getOneClient(id))
                }
                get("/last/{count}") {
                    val count = call.parameters["count"]?.toIntOrNull() ?: throw BadRequestException("Count must be int")
                    call.respond(eventService.getLast(count))
                }
                get("/period/{start}/{end}") {
                    val start = call.parameters["start"]?.toLongOrNull() ?: throw BadRequestException("Start must be int")
                    val end = call.parameters["end"]?.toLongOrNull() ?: throw BadRequestException("End must be int")
                    call.respond(eventService.getByPeriod(start, end))
                }
            }
        }
    }
}