package philarmonic.modules.files

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.utils.files.FilesUtil
import philarmonic.utils.kodein.KodeinController

class FilesController(override val di: DI) : KodeinController() {
    private val filesService: FilesService by instance()

    override fun Route.registerRoutes() {
        route("files") {
            get("{dir}/{module}/{file}") {
                val dir = call.parameters["dir"].toString()
                val module = call.parameters["module"].toString()
                val fileName = call.parameters["file"].toString()
                call.respondBytes(filesService.readFile(dir, module, fileName))
            }
        }
    }
}