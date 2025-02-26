package philarmonic.modules.document.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.document.data.dto.CreateDocumentDto
import philarmonic.modules.document.data.dto.UpdateDocumentDto
import philarmonic.modules.document.service.DocumentService

class DocumentController(override val di: DI) : KodeinController() {
    private val documentService: DocumentService by instance()

    override fun Route.registerRoutes() {
        route("/document") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreateDocumentDto>()
                    call.respond(documentService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdateDocumentDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(documentService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(documentService.delete(id))
                }
                patch("/visible/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(documentService.toggleVisible(id))
                }
                patch("/position/{id}/up") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(documentService.positionUp(id))
                }
                patch("/position/{id}/down") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(documentService.positionDown(id))
                }
            }
            get {
                call.respond(documentService.getAll())
            }
            route("client") {
                get {
                    call.respond(documentService.getAllClient())
                }
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(documentService.getOne(id))
            }
        }
    }
}