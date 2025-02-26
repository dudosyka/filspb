package philarmonic.utils.database.case_insensitive

import org.jetbrains.exposed.sql.*

class InsensitiveLikeOp(expr1: Expression<*>, expr2: Expression<*>) : ComparisonOp(expr1, expr2, "ILIKE")

infix fun ExpressionWithColumnType<String>.ilike(pattern: String): Op<Boolean> {
    return InsensitiveLikeOp(this, QueryParameter(pattern, columnType))
}

infix fun ExpressionWithColumnType<String?>.ilikeNullable(pattern: String): Op<Boolean> {
    return InsensitiveLikeOp(this, QueryParameter(pattern, columnType))
}