package dasturlash.homework2

import io.swagger.v3.oas.annotations.media.Schema

data class UserDTO(
    val id: Long?= null,
    @Schema(description = "User username", example = "polvon")
    val username: String,
    @Schema(description = "User email", example = "polvon@example.com")
    val email: String,
    @Schema(description = "Corporate", example = "true/false")
    val isCorporate: Boolean,
)

data class TransactionDTO(
    @Schema(description = "Account id/System - when type deposit", example = "1/System")
    val fromAccountId: Any,
    @Schema(description = "Recipient account id", example = "1")
    val toAccountId: Long,
    @Schema(description = "Amount of money", example = "25000.0")
    val amount: Double,
){
    data class TransferDTO(
        @Schema(description = "From account id", example = "1")
        val fromAccountId: Long,
        @Schema(description = "To account id", example = "2")
        val toAccountId: Long,
        val amount: Double,
    )
}
