package philarmonic.modules.document.data.dao


import philarmonic.utils.database.BaseIntEntity
import philarmonic.utils.database.BaseIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import philarmonic.modules.document.data.dto.DocumentDto
import philarmonic.modules.document.data.model.DocumentModel


class DocumentDao(id: EntityID<Int>) : BaseIntEntity<DocumentDto>(id, DocumentModel) {
    companion object : BaseIntEntityClass<DocumentDto, DocumentDao>(DocumentModel)

    var name by DocumentModel.name
    var doc by DocumentModel.doc
    var visible by DocumentModel.visible
    var position by DocumentModel.position

    override fun toOutputDto(): DocumentDto =
    DocumentDto(
        this.id.value,
        this.name,
        this.doc,
        this.visible,
        this.position,
    )
}