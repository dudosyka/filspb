package philarmonic.modules.files

import org.kodein.di.DI
import philarmonic.exceptions.NotFoundException
import philarmonic.utils.files.FilesUtil
import philarmonic.utils.kodein.KodeinService

class FilesService(di: DI) : KodeinService(di) {
    fun saveFileBase64(dir: String, module: String, fileName: String, base64: String): String {
        val newName = FilesUtil.buildName(fileName, module, dir)

        FilesUtil.upload(base64, newName)

        return "/$dir/$module/$newName"
    }

    fun readFile(dir: String, module: String, fileName: String): ByteArray {
        return FilesUtil.read(dir, module, fileName) ?: throw NotFoundException("File not found!")
    }
}