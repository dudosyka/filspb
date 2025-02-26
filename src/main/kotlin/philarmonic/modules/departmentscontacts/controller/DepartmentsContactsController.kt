package philarmonic.modules.departmentscontacts.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.departmentscontacts.data.dto.CreateDepartmentsContactsDto
import philarmonic.modules.departmentscontacts.data.dto.UpdateDepartmentsContactsDto
import philarmonic.modules.departmentscontacts.service.DepartmentsContactsService

class DepartmentsContactsController(override val di: DI) : KodeinController() {
    private val departmentsContactsService: DepartmentsContactsService by instance()

    override fun Route.registerRoutes() {
        route("/department/contacts") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreateDepartmentsContactsDto>()
                    call.respond(departmentsContactsService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdateDepartmentsContactsDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(departmentsContactsService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(departmentsContactsService.delete(id))
                }
                patch("/visible/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(departmentsContactsService.toggleVisible(id))
                }
                patch("/position/{id}/up") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(departmentsContactsService.positionUp(id))
                }
                patch("/position/{id}/down") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(departmentsContactsService.positionDown(id))
                }
            }
            get {
                call.respond(departmentsContactsService.getAll())
            }
            route("client") {
                get {
                    call.respond(departmentsContactsService.getAllClient())
                }
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(departmentsContactsService.getOne(id))
            }
        }
    }
}