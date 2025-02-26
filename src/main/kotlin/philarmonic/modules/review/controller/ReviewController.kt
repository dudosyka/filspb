package philarmonic.modules.review.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.review.data.dto.CreateReviewDto
import philarmonic.modules.review.data.dto.UpdateReviewDto
import philarmonic.modules.review.service.ReviewService
import philarmonic.plugins.Logger

class ReviewController(override val di: DI) : KodeinController() {
    private val reviewService: ReviewService by instance()

    override fun Route.registerRoutes() {
        route("/review") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreateReviewDto>()
                    call.respond(reviewService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdateReviewDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(reviewService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(reviewService.delete(id))
                }
                patch("/visible/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(reviewService.toggleVisible(id))
                }
                patch("/position/{id}/up") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(reviewService.positionUp(id))
                }
                patch("/position/{id}/down") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(reviewService.positionDown(id))
                }
            }
            get {
                call.respond(reviewService.getAll())
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(reviewService.getOne(id))
            }
            route("client") {
                post {
                    val createReviewDto = call.receive<CreateReviewDto>()
                    Logger.debug(createReviewDto)
                    val result = reviewService.create(createReviewDto)
                    Logger.debug(result)
                    call.respond(result)
                }
            }
        }
    }
}