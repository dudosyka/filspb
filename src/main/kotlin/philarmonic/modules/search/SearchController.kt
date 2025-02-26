package philarmonic.modules.search

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.utils.kodein.KodeinController

class SearchController(override val di: DI) : KodeinController() {
    private val searchService: SearchService by instance()
    /**
     * Method that subtypes must override to register the handled [Routing] routes.
     */
    override fun Route.registerRoutes() {
        route("search/all/client") {
            get("{query}") {
                val query = call.parameters["query"].toString()
                call.respond<List<SearchResultDto>>(searchService.find(query))
            }
        }
    }
}