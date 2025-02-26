package philarmonic.utils.files

import io.ktor.util.date.*
import philarmonic.conf.AppConf
import philarmonic.exceptions.BadRequestException
import philarmonic.exceptions.NotFoundException
import philarmonic.plugins.Logger
import java.util.*
import kotlin.io.path.*


object FilesUtil {

    fun buildName(file: String, module: String, dir: String = "uploads"): String {
        val currentMillis = getTimeMillis()

        val fileName = Path(file)
        return "/$dir/$module/${fileName.name}${currentMillis}.${fileName.extension}"
    }

    fun upload(base64Encoded: String, fileName: String) {
        try {
            Logger.debug(base64Encoded)
            val bytes = Base64.getDecoder().decode(base64Encoded)
            val path = Path("${AppConf.server.fileLocation}$fileName")
            path.writeBytes(bytes)
        } catch (e: Exception) {
            Logger.debug(e)
            throw BadRequestException("Bad file encoding")
        }
    }

    fun read(dir: String, module: String, fileName: String): ByteArray? {
        return try {
            Path("${AppConf.server.fileLocation}/$dir/$module/$fileName").readBytes()
        } catch (e: Exception) {
            null
        }
    }

    fun encodeBytes(bytes: ByteArray?): String {
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun getBytes(base64Mime: String): ByteArray {
        return try {
            Base64.getMimeDecoder().decode(base64Mime)
        } catch (e: Exception) {
            throw NotFoundException("File not found")
        }
    }

    fun removeFile(fileName: String): Boolean {
        return Path("${AppConf.server.fileLocation}$fileName").deleteIfExists()
    }
}