package philarmonic.modules.seasonevent.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.seasonevent.data.dto.CreateSeasonEventDto
import philarmonic.modules.seasonevent.data.dto.UpdateSeasonEventDto
import philarmonic.modules.seasonevent.service.SeasonEventService

class SeasonEventController(override val di: DI) : KodeinController() {
    private val eventService: SeasonEventService by instance()

    override fun Route.registerRoutes() {
        route("/event/season") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreateSeasonEventDto>()
                    call.respond(eventService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdateSeasonEventDto>()
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
                get {
                    call.respond(eventService.getAllClient())
                }
                get("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(eventService.getOneClient(id))
                }
                get("/last/{count}") {
                    val count = call.parameters["count"]?.toIntOrNull() ?: throw BadRequestException("Count must be int")
                    call.respond(eventService.getLast(count))
                }
            }
        }
    }
}