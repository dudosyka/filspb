package philarmonic.modules.people.data.model


import org.jetbrains.exposed.sql.ReferenceOption
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.default
import philarmonic.modules.childrenprograms.data.model.ChildrenProgramsModel.nullable
import philarmonic.modules.peoplecategory.data.model.PeopleCategoryModel
import philarmonic.utils.database.BaseIntIdTable

object PeopleModel: BaseIntIdTable() {
    val name = text("name")
    val photo = text("photo").nullable().default(null)
    val workPosition = text("workPosition").nullable().default(null)
    val category = reference("category", PeopleCategoryModel, ReferenceOption.CASCADE, ReferenceOption.RESTRICT).nullable().default(null)
    val visible = bool("visible")
    val position = integer("position")
}