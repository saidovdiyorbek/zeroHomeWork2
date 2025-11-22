package dasturlash.homework2

import io.swagger.v3.oas.annotations.media.Schema

data class UserDTO(
    @Schema(description = "User username", example = "polvon")
    val username: String,
    @Schema(description = "User email", example = "polvon@example.com")
    val email: String,
    @Schema(description = "Corporate", example = "true/false")
    val isCorporate: Boolean,
)
