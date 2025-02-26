package philarmonic.modules.partners.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.partners.data.dto.CreatePartnersDto
import philarmonic.modules.partners.data.dto.UpdatePartnersDto
import philarmonic.modules.partners.service.PartnersService

class PartnersController(override val di: DI) : KodeinController() {
    private val partnersService: PartnersService by instance()

    override fun Route.registerRoutes() {
        route("/partners") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreatePartnersDto>()
                    call.respond(partnersService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdatePartnersDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(partnersService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(partnersService.delete(id))
                }
                patch("/visible/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(partnersService.toggleVisible(id))
                }
                patch("/position/{id}/up") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(partnersService.positionUp(id))
                }
                patch("/position/{id}/down") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(partnersService.positionDown(id))
                }
            }
            get {
                call.respond(partnersService.getAll())
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(partnersService.getOne(id))
            }
            route("client") {
                get {
                   call.respond(partnersService.getAllClient())
                }
            }
        }
    }
}