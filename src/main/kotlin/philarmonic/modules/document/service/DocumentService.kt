package philarmonic.modules.document.service

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import philarmonic.exceptions.BadRequestException
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import org.kodein.di.instance
import philarmonic.modules.departmentscontacts.data.dao.DepartmentsContactsDao
import philarmonic.modules.departmentscontacts.data.model.DepartmentsContactsModel
import philarmonic.modules.document.data.model.DocumentModel
import philarmonic.modules.document.data.dao.DocumentDao
import philarmonic.modules.document.data.dto.*
import philarmonic.modules.files.FilesService
import philarmonic.utils.files.FilesUtil

class DocumentService(di: DI) : KodeinService(di) {
    private val fileService: FilesService by instance()

    fun create(createDto: CreateDocumentDto): DocumentDto = transaction {
        val uploaded = fileService.saveFileBase64("uploads", "documents", createDto.docName ?: "", createDto.doc  ?: "")

        val created = try { DocumentDao.new {
            this.name = createDto.name
            this.doc = uploaded
            this.visible = createDto.visible
            this.position = createDto.position
        } } catch (e: Exception) {
            FilesUtil.removeFile(uploaded)
            throw BadRequestException("")
        }

        created.toOutputDto()
    }

    fun getAll(): List<DocumentListDto> = transaction {
        DocumentModel
            .selectAll()
            .orderBy(DocumentModel.position)
            .map {
                DocumentListDto(
                    it[DocumentModel.id].value,
                    it[DocumentModel.name],
                    it[DocumentModel.visible],
                    it[DocumentModel.position]
                )
            }
    }

    fun getOne(id: Int): DocumentDto = transaction {
        DocumentDao[id].toOutputDto()
    }

    fun update(id: Int, updateDto: UpdateDocumentDto): DocumentDto = transaction {
        val dao = DocumentDao[id]
        dao.name = updateDto.name ?: dao.name

        val oldFile = dao.doc
        if (updateDto.doc != null && updateDto.docName != null) {
            val uploaded = fileService.saveFileBase64("uploads", "documents", updateDto.docName, updateDto.doc)
            dao.doc = uploaded
        }
        dao.doc = updateDto.doc ?: dao.doc
        dao.visible = updateDto.visible ?: dao.visible
        dao.position = updateDto.position ?: dao.position

        dao.flush()

        if (updateDto.doc != null && updateDto.docName != null && oldFile != dao.doc) {
            FilesUtil.removeFile(oldFile ?: "")
        }

        dao.toOutputDto()
    }

    fun delete(id: Int): Boolean = transaction {
        val dao = DocumentDao[id]

        dao.delete()

        true
    }

    fun toggleVisible(id: Int): DocumentDto = transaction {
        val dao = DocumentDao[id]

        dao.visible = !dao.visible

        dao.flush()

        dao.toOutputDto()
    }

    fun positionUp(id: Int): DocumentDto = transaction {
        val dao = DocumentDao[id]
        if (dao.position == 0)
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position - 1
        var top = DocumentDao.find { DocumentModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition >= 0 && top == null) {
            top = DocumentDao.find { DocumentModel.position eq (dao.position + 1) }.firstOrNull()
            prevPosition += 1
        }

        if (top == null) {
            dao.position = 0
        } else {
            val topPos = top.position
            top.position = dao.position
            dao.position = topPos

            top.flush()
        }

        dao.flush()

        dao.toOutputDto()
    }

    fun positionDown(id: Int): DocumentDto = transaction {
        val allCount = DocumentDao.all().count()
        val dao = DocumentDao[id]
        if (dao.position == (allCount - 1).toInt())
            return@transaction dao.toOutputDto()

        var prevPosition = dao.position + 1
        var bottom = DocumentDao.find { DocumentModel.position eq (dao.position + 1) }.firstOrNull()
        while (prevPosition < allCount && bottom == null) {
            bottom = DocumentDao.find { DocumentModel.position eq (dao.position + 1) }.firstOrNull()
            prevPosition += 1
        }

        if (bottom == null) {
            dao.position = (allCount - 1).toInt()
        } else {
            val topPos = bottom.position
            bottom.position = dao.position
            dao.position = topPos

            bottom.flush()
        }

        dao.flush()

        dao.toOutputDto()
    }

    fun getAllClient(): List<DocumentClientDto> = transaction {
        DocumentModel
            .select(DocumentModel.name, DocumentModel.doc)
            .where { DocumentModel.visible eq true }
            .map {
                DocumentClientDto(
                    it[DocumentModel.name],
                    it[DocumentModel.doc] ?: ""
                )
            }
    }
}