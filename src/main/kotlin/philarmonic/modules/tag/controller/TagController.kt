package philarmonic.modules.tag.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.tag.data.dto.CreateTagDto
import philarmonic.modules.tag.data.dto.UpdateTagDto
import philarmonic.modules.tag.service.TagService

class TagController(override val di: DI) : KodeinController() {
    private val tagService: TagService by instance()

    override fun Route.registerRoutes() {
        route("/tag") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreateTagDto>()
                    call.respond(tagService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdateTagDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(tagService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(tagService.delete(id))
                }
                patch("/visible/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(tagService.toggleVisible(id))
                }
                patch("/position/{id}/up") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(tagService.positionUp(id))
                }
                patch("/position/{id}/down") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(tagService.positionDown(id))
                }
            }
            get {
                call.respond(tagService.getAll())
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(tagService.getOne(id))
            }
            route("client") {
                get {
                    call.respond(tagService.getAllClient())
                }
            }
        }
    }
}