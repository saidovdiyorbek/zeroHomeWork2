package dasturlash.homework2

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDate
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler{

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(e: UserNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(e.statusCode).body(
            ErrorResponse(
                success = false,
                message = e.message ?: "User not found",
                errorCode = "user_not_found",
                statusCode = e.statusCode.value(),
                timestamp = LocalDateTime.now()
            )
        )
    }

    @ExceptionHandler(UserNotCorporateException::class)
    fun handleUserNotCorporateException(e: UserNotCorporateException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(e.statusCode).body(
            ErrorResponse(
                success = false,
                message = e.message ?: "User not corporate",
                errorCode = "user_not_corporate",
                statusCode = e.statusCode.value(),
                timestamp = LocalDateTime.now()
            )
        )
    }

    @ExceptionHandler(UserLimitIsOverException::class)
    fun handleUserLimitIsOverException(e: UserLimitIsOverException): ResponseEntity<ErrorResponse>{
        return ResponseEntity.status(e.statusCode).body(
            ErrorResponse(
                success = false,
                message = e.message ?: "User limit is over",
                errorCode = "user_limit_is_over",
                statusCode = e.statusCode.value(),
                timestamp = LocalDateTime.now()
            )
        )
    }
}

class ErrorResponse(
    val success: Boolean,
    val message: String,
    val errorCode: String? = null,
    val statusCode: Int,
    val timestamp: LocalDateTime = LocalDateTime.now()
)