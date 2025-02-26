package philarmonic.modules.auth.service


import philarmonic.exceptions.ForbiddenException
import io.ktor.util.date.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import philarmonic.utils.kodein.KodeinService
import org.kodein.di.DI
import philarmonic.exceptions.BadRequestException
import philarmonic.modules.auth.data.dto.AuthInputDto
import philarmonic.modules.auth.data.dto.RegisterInputDto
import philarmonic.modules.auth.data.dto.UpdateAdminDto
import philarmonic.modules.auth.data.model.UserLoginModel
import philarmonic.modules.user.dto.UserDto
import philarmonic.modules.user.model.UserModel
import philarmonic.utils.security.bcrypt.CryptoUtil
import philarmonic.utils.security.jwt.AuthorizedUser
import philarmonic.utils.security.jwt.JwtUtil
import philarmonic.utils.security.jwt.RefreshDto
import philarmonic.utils.security.jwt.TokenPairDto
import java.util.NoSuchElementException

class AuthService(di: DI): KodeinService(di) {
    fun auth(authInputDto: AuthInputDto): TokenPairDto = transaction {
        val user: Pair<Int, String> = try {
            UserModel.getByLogin(authInputDto.login)
        } catch (e: NoSuchElementException) {
            throw ForbiddenException()
        }
        val lastLogin = getTimeMillis()

        if (!CryptoUtil.compare(authInputDto.password, user.second) ||
            UserLoginModel.setLastLogin(user.first, lastLogin) == 0)
            throw ForbiddenException()

        TokenPairDto(
            JwtUtil.createToken(user.first),
            JwtUtil.createToken(user.first, lastLogin)
        )
    }

    fun refresh(refreshDto: RefreshDto): TokenPairDto = transaction {
        val lastLogin = UserLoginModel.getLastLogin(refreshDto.id)

        if (lastLogin != refreshDto.lastLogin)
            throw ForbiddenException()

        if (UserLoginModel.setLastLogin(refreshDto.id, lastLogin) == 0)
            throw ForbiddenException()

        TokenPairDto(
            JwtUtil.createToken(refreshDto.id),
            JwtUtil.createToken(refreshDto.id, lastLogin)
        )
    }

    fun getAuthorized(authorized: AuthorizedUser): UserDto = transaction {
        UserModel.getOne(authorized.id) ?: throw ForbiddenException()
    }

    fun createNew(registerInputDto: RegisterInputDto): TokenPairDto = transaction {
        val count = UserModel.select(UserModel.id).where { UserModel.login eq registerInputDto.login }.count()
        if (count > 0) throw ForbiddenException()

        UserModel.insert {
            it[login] = registerInputDto.login
            it[hash] = CryptoUtil.hash(registerInputDto.pass)
        }

        commit()

        auth(AuthInputDto(registerInputDto.login, registerInputDto.pass))
    }

    fun updateAdmin(updateAdminDto: UpdateAdminDto) = transaction {
        val count = UserModel.select(UserModel.id).where {
            (UserModel.login eq updateAdminDto.login) and (UserModel.id neq updateAdminDto.id)
        }.count()
        if (count > 0) throw ForbiddenException()

        UserModel.update({UserModel.id eq updateAdminDto.id}) {
            it[login] = updateAdminDto.login
            it[hash] = CryptoUtil.hash(updateAdminDto.pass)
        }

        commit()
    }

    fun getAdmins(): List<UserDto> = transaction {
        UserModel.select(UserModel.login, UserModel.id).map {
            UserDto(it[UserModel.id].value, it[UserModel.login])
        }
    }

    fun getAdmin(id: Int): UserDto = transaction {
        UserModel.getOne(id) ?: throw BadRequestException("Bad id provided")
    }
}