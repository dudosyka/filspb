package philarmonic.modules.slider.controller

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
import philarmonic.modules.slider.data.dto.CreateSliderDto
import philarmonic.modules.slider.data.dto.UpdateSliderDto
import philarmonic.modules.slider.service.SliderService

class SliderController(override val di: DI) : KodeinController() {
    private val sliderService: SliderService by instance()

    override fun Route.registerRoutes() {
        route("/slider") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreateSliderDto>()
                    call.respond(sliderService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdateSliderDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(sliderService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(sliderService.delete(id))
                }
                patch("/visible/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(sliderService.toggleVisible(id))
                }
                patch("/position/{id}/up") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(sliderService.positionUp(id))
                }
                patch("/position/{id}/down") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(sliderService.positionDown(id))
                }
            }
            get {
                call.respond(sliderService.getAll())
            }
            route("client") {
                get {
                    call.respond(sliderService.getAllClient())
                }
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(sliderService.getOne(id))
            }
        }
    }
}