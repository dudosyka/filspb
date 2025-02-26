package philarmonic.modules.vacancy.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.vacancy.data.dto.CreateVacancyDto
import philarmonic.modules.vacancy.data.dto.UpdateVacancyDto
import philarmonic.modules.vacancy.service.VacancyService

class VacancyController(override val di: DI) : KodeinController() {
    private val vacancyService: VacancyService by instance()

    override fun Route.registerRoutes() {
        route("/vacancy") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreateVacancyDto>()
                    call.respond(vacancyService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdateVacancyDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(vacancyService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(vacancyService.delete(id))
                }
                patch("/visible/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(vacancyService.toggleVisible(id))
                }
                patch("/position/{id}/up") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(vacancyService.positionUp(id))
                }
                patch("/position/{id}/down") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(vacancyService.positionDown(id))
                }
            }
            get {
                call.respond(vacancyService.getAll())
            }
            route("client") {
                get {
                    call.respond(vacancyService.getAllClient())
                }
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(vacancyService.getOne(id))
            }
        }
    }
}