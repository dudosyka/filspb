package philarmonic.modules.search

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import philarmonic.modules.event.data.model.EventModel
import philarmonic.modules.eventsecond.data.model.EventSecondModel
import philarmonic.modules.news.data.model.NewsModel
import philarmonic.utils.database.case_insensitive.ilike
import philarmonic.utils.database.case_insensitive.ilikeNullable
import philarmonic.utils.kodein.KodeinService

class SearchService(di: DI) : KodeinService(di) {
    fun find(query: String): List<SearchResultDto> = transaction {
        val newsRes = NewsModel
            .select(NewsModel.id, NewsModel.name, NewsModel.shortDescription, NewsModel.image)
            .where {
                (NewsModel.visible eq true) and (
                        (NewsModel.shortDescription ilikeNullable "%$query%") or
                        (NewsModel.name ilike "%$query%") or
                        (NewsModel.description ilikeNullable "%$query%")
                )
            }
            .map {
                SearchResultDto(
                    it[NewsModel.id].value,
                    it[NewsModel.name],
                    it[NewsModel.shortDescription] ?: "",
                    it[NewsModel.image] ?: "",
                    true
                )
            }

        val eventRes = EventModel
            .leftJoin(EventSecondModel)
            .select(EventModel.id, EventSecondModel.name, EventSecondModel.shortDescription, EventSecondModel.image)
            .where {
                (EventSecondModel.visible eq true) and (EventModel.visible eq true) and (
                    (EventSecondModel.shortDescription ilikeNullable "%$query%") or
                    (EventSecondModel.name ilike "%$query%") or
                    (EventSecondModel.description ilikeNullable "%$query%")
                )
            }
            .map {
                SearchResultDto(
                    it[EventModel.id].value,
                    it[EventSecondModel.name],
                    it[EventSecondModel.shortDescription] ?: "",
                    it[EventSecondModel.image] ?: "",
                    false
                )
            }

        val res = newsRes + eventRes

        res.shuffled()
    }
}