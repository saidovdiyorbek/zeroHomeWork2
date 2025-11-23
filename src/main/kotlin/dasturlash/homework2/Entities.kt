package dasturlash.homework2

import java.time.LocalDateTime

data class UserEntity(

    val id: Long? = 0,
    val username: String,
    val email: String,
    val isCorporate : Boolean,
    val createdDate: LocalDateTime = LocalDateTime.now(),
)

data class AccountEntity(
    val id: Long = 0,
    val userId: Long,
    val balance: Double = 0.0,
    val createdDate: LocalDateTime = LocalDateTime.now()

)

data class TransactionEntity(
    val id: Long,
    val fromAccountId: Long,
    val toAccountId: Long,
    val amount: Double,
    val transactionStatus: TransactionStatus,
    val createdDate: LocalDateTime = LocalDateTime.now()
)