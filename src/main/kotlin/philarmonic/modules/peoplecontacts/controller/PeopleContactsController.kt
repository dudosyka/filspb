package philarmonic.modules.peoplecontacts.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.peoplecontacts.data.dto.CreatePeopleContactsDto
import philarmonic.modules.peoplecontacts.data.dto.UpdatePeopleContactsDto
import philarmonic.modules.peoplecontacts.service.PeopleContactsService

class PeopleContactsController(override val di: DI) : KodeinController() {
    private val peopleContactsService: PeopleContactsService by instance()

    override fun Route.registerRoutes() {
        route("/people/contacts") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreatePeopleContactsDto>()
                    call.respond(peopleContactsService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdatePeopleContactsDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(peopleContactsService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(peopleContactsService.delete(id))
                }
                patch("/visible/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(peopleContactsService.toggleVisible(id))
                }
                patch("/position/{id}/up") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(peopleContactsService.positionUp(id))
                }
                patch("/position/{id}/down") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(peopleContactsService.positionDown(id))
                }
            }
            get {
                call.respond(peopleContactsService.getAll())
            }

            route("client") {
                get {
                    call.respond(peopleContactsService.getAllClient())
                }
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(peopleContactsService.getOne(id))
            }
        }
    }
}