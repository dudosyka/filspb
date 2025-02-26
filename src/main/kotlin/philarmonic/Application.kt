package philarmonic

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import philarmonic.conf.AppConf
import philarmonic.modules.auth.controller.AuthController
import philarmonic.modules.auth.data.model.UserLoginModel
import philarmonic.modules.auth.service.AuthService
import philarmonic.plugins.*
import philarmonic.utils.database.DatabaseConnector
import philarmonic.utils.kodein.bindSingleton
import philarmonic.utils.kodein.kodeinApplication
import philarmonic.modules.news.service.NewsService 
import philarmonic.modules.news.controller.NewsController 
import philarmonic.modules.news.data.model.NewsModel
import philarmonic.modules.platform.service.PlatformService 
import philarmonic.modules.platform.controller.PlatformController 
import philarmonic.modules.platform.data.model.PlatformModel
import philarmonic.modules.people.service.PeopleService 
import philarmonic.modules.people.controller.PeopleController 
import philarmonic.modules.people.data.model.PeopleModel
import philarmonic.modules.tag.service.TagService 
import philarmonic.modules.tag.controller.TagController 
import philarmonic.modules.tag.data.model.TagModel
import philarmonic.modules.eventsecond.service.EventSecondService 
import philarmonic.modules.eventsecond.controller.EventSecondController 
import philarmonic.modules.eventsecond.data.model.EventSecondModel
import philarmonic.modules.vacancy.service.VacancyService 
import philarmonic.modules.vacancy.controller.VacancyController 
import philarmonic.modules.vacancy.data.model.VacancyModel
import philarmonic.modules.peoplecategory.service.PeopleCategoryService 
import philarmonic.modules.peoplecategory.controller.PeopleCategoryController 
import philarmonic.modules.peoplecategory.data.model.PeopleCategoryModel
import philarmonic.modules.peoplecontacts.service.PeopleContactsService 
import philarmonic.modules.peoplecontacts.controller.PeopleContactsController 
import philarmonic.modules.peoplecontacts.data.model.PeopleContactsModel
import philarmonic.modules.review.service.ReviewService 
import philarmonic.modules.review.controller.ReviewController 
import philarmonic.modules.review.data.model.ReviewModel
import philarmonic.modules.event.service.EventService 
import philarmonic.modules.event.controller.EventController 
import philarmonic.modules.event.data.model.EventModel
import philarmonic.modules.document.service.DocumentService 
import philarmonic.modules.document.controller.DocumentController 
import philarmonic.modules.document.data.model.DocumentModel
import philarmonic.modules.departmentscontacts.service.DepartmentsContactsService 
import philarmonic.modules.departmentscontacts.controller.DepartmentsContactsController 
import philarmonic.modules.departmentscontacts.data.model.DepartmentsContactsModel
import philarmonic.modules.partners.service.PartnersService 
import philarmonic.modules.partners.controller.PartnersController 
import philarmonic.modules.partners.data.model.PartnersModel
import philarmonic.modules.childrenprograms.service.ChildrenProgramsService 
import philarmonic.modules.childrenprograms.controller.ChildrenProgramsController 
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel
import philarmonic.modules.eventsecond.data.model.EventSecondToTagsModel
import philarmonic.modules.files.FilesController
import philarmonic.modules.files.FilesService
import philarmonic.modules.systemconfiguration.service.SystemConfigurationService 
import philarmonic.modules.systemconfiguration.controller.SystemConfigurationController 
import philarmonic.modules.systemconfiguration.data.model.SystemConfigurationModel
import philarmonic.modules.hall.service.HallService 
import philarmonic.modules.hall.controller.HallController 
import philarmonic.modules.hall.data.model.HallModel
import philarmonic.modules.search.SearchController
import philarmonic.modules.search.SearchService
import philarmonic.modules.seasonevent.controller.SeasonEventController
import philarmonic.modules.seasonevent.data.model.SeasonEventModel
import philarmonic.modules.seasonevent.data.model.SeasonEventToEventModel
import philarmonic.modules.seasonevent.service.SeasonEventService
import philarmonic.modules.slider.controller.SliderController
import philarmonic.modules.slider.data.model.SliderModel
import philarmonic.modules.slider.service.SliderService
import philarmonic.modules.user.model.UserModel
import philarmonic.modules.usertouser.UserToUserModel
import philarmonic.utils.security.bcrypt.CryptoUtil


fun main() {
    embeddedServer(Netty, port = AppConf.server.port, host = AppConf.server.host, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureCORS()
    configureSerialization()
    configureSockets()
    configureExceptionFilter()

    kodeinApplication("/") {
        // ----- Services ------
        bindSingleton { NewsService(it) }
        bindSingleton { PlatformService(it) }
        bindSingleton { PeopleService(it) }
        bindSingleton { TagService(it) }
        bindSingleton { EventSecondService(it) }
        bindSingleton { VacancyService(it) }
        bindSingleton { PeopleCategoryService(it) }
        bindSingleton { PeopleContactsService(it) }
        bindSingleton { ReviewService(it) }
        bindSingleton { EventService(it) }
        bindSingleton { DocumentService(it) }
        bindSingleton { DepartmentsContactsService(it) }
        bindSingleton { PartnersService(it) }
        bindSingleton { ChildrenProgramsService(it) }
        bindSingleton { SystemConfigurationService(it) }
        bindSingleton { HallService(it) }
        bindSingleton { FilesService(it) }
        bindSingleton { SearchService(it) }
        bindSingleton { SliderService(it) }
        bindSingleton { SeasonEventService(it) }
        bindSingleton { AuthService(it) }

        // ----- Controllers ------
        bindSingleton { NewsController(it) }
        bindSingleton { PlatformController(it) }
        bindSingleton { PeopleController(it) }
        bindSingleton { TagController(it) }
        bindSingleton { EventSecondController(it) }
        bindSingleton { VacancyController(it) }
        bindSingleton { PeopleCategoryController(it) }
        bindSingleton { PeopleContactsController(it) }
        bindSingleton { ReviewController(it) }
        bindSingleton { EventController(it) }
        bindSingleton { DocumentController(it) }
        bindSingleton { DepartmentsContactsController(it) }
        bindSingleton { PartnersController(it) }
        bindSingleton { ChildrenProgramsController(it) }
        bindSingleton { SystemConfigurationController(it) }
        bindSingleton { HallController(it) }
        bindSingleton { FilesController(it) }
        bindSingleton { SearchController(it) }
        bindSingleton { SliderController(it) }
        bindSingleton { SeasonEventController(it) }
        bindSingleton { AuthController(it) }
    }

    DatabaseConnector(
        UserModel, UserLoginModel,
        NewsModel,
        PlatformModel,
        PeopleModel,
        TagModel,
        EventSecondModel,
        EventSecondToTagsModel,
        VacancyModel,
        PeopleCategoryModel,
        PeopleContactsModel,
        ReviewModel,
        EventModel,
        DocumentModel,
        DepartmentsContactsModel,
        PartnersModel,
        ChildrenProgramsModel,
        SystemConfigurationModel,
        HallModel,
        SliderModel,
        SeasonEventModel,
        SeasonEventToEventModel,
        UserToUserModel
    ) {
        if (UserModel.selectAll().count() < 1) {
            UserModel.insert {
                it[login] = "admin"
                it[hash] = CryptoUtil.hash("123")
            }
        }
        if (SystemConfigurationModel.selectAll().count() < 1) {
            SystemConfigurationModel.insert {
                it[name] = "89110885444"
                it[email] = "test@test.test"
                it[address] = "address"
                it[city] = "city"
                it[addressShort] = "short address"
                it[vk] = ""
                it[telegram] = ""
                it[soundCloud] = ""
            }
        }
    }

}
