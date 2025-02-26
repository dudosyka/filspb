package philarmonic.modules.auth.controller


import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import philarmonic.utils.kodein.KodeinController
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.auth.data.dto.AuthInputDto
import philarmonic.modules.auth.data.dto.RegisterInputDto
import philarmonic.modules.auth.data.dto.UpdateAdminDto
import philarmonic.modules.auth.service.AuthService

class AuthController(override val di: DI) : KodeinController() {
    private val authService: AuthService by instance()
    override fun Route.registerRoutes() {
        route("auth") {
            post {
                val authInputDto = call.receive<AuthInputDto>()

                call.respond(authService.auth(authInputDto))
            }
            route("admin") {
                get {
                    call.respond(authService.getAdmins())
                }
                get("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("User id must be INT")

                    call.respond(authService.getAdmin(id))
                }
                post {
                    val registerInputDto = call.receive<RegisterInputDto>()

                    call.respond(authService.createNew(registerInputDto))
                }
                patch("{id}") {
                    val dto = call.receive<RegisterInputDto>()
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("User id must be INT")
                    val updateAdminDto = UpdateAdminDto(id, dto.login, dto.pass)

                    call.respond(authService.updateAdmin(updateAdminDto))
                }
            }

            authenticate("default") {
                get("authorized") {
                    call.respond(authService.getAuthorized(call.getAuthorized()))
                }
            }

            authenticate("refresh") {
                post("refresh") {
                    val refreshDto = call.getRefreshed();

                    call.respond(authService.refresh(refreshDto))
                }
            }
        }
    }

}