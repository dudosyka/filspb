package philarmonic.modules.people.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.people.data.dto.CreatePeopleDto
import philarmonic.modules.people.data.dto.UpdatePeopleDto
import philarmonic.modules.people.service.PeopleService

class PeopleController(override val di: DI) : KodeinController() {
    private val peopleService: PeopleService by instance()

    override fun Route.registerRoutes() {
        route("/people") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreatePeopleDto>()
                    call.respond(peopleService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdatePeopleDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(peopleService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(peopleService.delete(id))
                }
                patch("/visible/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(peopleService.toggleVisible(id))
                }
                patch("/position/{id}/up") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(peopleService.positionUp(id))
                }
                patch("/position/{id}/down") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(peopleService.positionDown(id))
                }
            }
            get {
                call.respond(peopleService.getAll())
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(peopleService.getOne(id))
            }
            route("client") {
                get {
                    call.respond(peopleService.getAllClient())
                }
            }
        }
    }
}