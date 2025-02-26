package philarmonic.modules.usertouser

import philarmonic.modules.user.model.UserModel
import philarmonic.utils.database.BaseIntIdTable

object UserToUserModel : BaseIntIdTable() {
    val user = reference("user", UserModel)
    val friend = reference("friend", UserModel)
}