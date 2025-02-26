package philarmonic.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
data class ExceptionOutput (
    val status: Int,
    val message: String
)

fun Application.configureExceptionFilter() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            Logger.debug(cause.stackTraceToString())
            call.respond(HttpStatusCode.InternalServerError, ExceptionOutput(500, "$cause"))
        }
    }
}
