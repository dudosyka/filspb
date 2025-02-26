package philarmonic.utils.database

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.withSuspendTransaction
import java.sql.Connection.TRANSACTION_READ_COMMITTED

interface TransactionalService {
    suspend fun <T> transaction(statements: suspend Transaction.() -> T) = TransactionManager.currentOrNew(TRANSACTION_READ_COMMITTED).withSuspendTransaction {
        try {
            statements()
        } catch (e: Exception) {
            throw e
        } catch (e: ExposedSQLException) {
            throw e
        }
    }


}