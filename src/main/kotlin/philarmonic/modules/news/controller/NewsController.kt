package philarmonic.modules.news.controller


import philarmonic.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.news.data.dto.CreateNewsDto
import philarmonic.modules.news.data.dto.UpdateNewsDto
import philarmonic.modules.news.service.NewsService

class NewsController(override val di: DI) : KodeinController() {
    private val newsService: NewsService by instance()

    override fun Route.registerRoutes() {
        route("/news") {
            authenticate("default") {
                post {
                    val createDto = call.receive<CreateNewsDto>()
                    call.respond(newsService.create(createDto))
                }
                patch("{id}") {
                    val updateDto = call.receive<UpdateNewsDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(newsService.update(id, updateDto))
                }
                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(newsService.delete(id))
                }
                patch("/visible/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(newsService.toggleVisible(id))
                }
                patch("/position/{id}/up") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(newsService.positionUp(id))
                }
                patch("/position/{id}/down") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(newsService.positionDown(id))
                }
            }
            get {
                call.respond(newsService.getAll())
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                call.respond(newsService.getOne(id))
            }
            route("client") {
                get("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Id must be Int")
                    call.respond(newsService.getOneClient(id))
                }
                get {
                    call.respond(newsService.getAllClient(0))
                }
                get("/last/{count}") {
                    val count = call.parameters["count"]?.toIntOrNull() ?: throw BadRequestException("Count must be int")
                    call.respond(newsService.getAllClient(count))
                }
            }
        }
    }
}