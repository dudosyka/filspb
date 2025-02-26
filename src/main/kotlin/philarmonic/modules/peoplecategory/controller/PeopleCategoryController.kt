package philarmonic.modules.peoplecategory.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.peoplecategory.data.dto.CreatePeopleCategoryDto
import philarmonic.modules.peoplecategory.data.dto.UpdatePeopleCategoryDto
import philarmonic.modules.peoplecategory.service.PeopleCategoryService

class PeopleCategoryController(override val di: DI) : KodeinController() {
    private val peopleCategoryService: PeopleCategoryService by instance()

    override fun Route.registerRoutes() {
        route("/people/category") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreatePeopleCategoryDto>()
                    call.respond(peopleCategoryService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdatePeopleCategoryDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(peopleCategoryService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(peopleCategoryService.delete(id))
                }
                patch("/visible/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(peopleCategoryService.toggleVisible(id))
                }
                patch("/position/{id}/up") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(peopleCategoryService.positionUp(id))
                }
                patch("/position/{id}/down") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(peopleCategoryService.positionDown(id))
                }
            }
            get {
                call.respond(peopleCategoryService.getAll())
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(peopleCategoryService.getOne(id))
            }
        }
    }
}