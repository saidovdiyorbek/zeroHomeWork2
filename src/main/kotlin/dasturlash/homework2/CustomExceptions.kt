package dasturlash.homework2

import org.springframework.http.HttpStatus

class UserNotFoundException(message: String,
    val statusCode: HttpStatus = HttpStatus.NOT_FOUND
) : RuntimeException(message)

class UserNotCorporateException(message: String = "You are not corporate user", val statusCode: HttpStatus = HttpStatus.TOO_MANY_REQUESTS)
    : RuntimeException(message)

class UserLimitIsOverException(message: String = "Your limit is over, you have 10 accounts", val statusCode: HttpStatus = HttpStatus.TOO_MANY_REQUESTS)
: RuntimeException(message)

class AccountNotFoundException(message: String = "Account not found",  val statusCode: HttpStatus = HttpStatus.NOT_FOUND) : RuntimeException(message)
class InvalidAmountException(message: String = "Invalid amount",  val statusCode: HttpStatus = HttpStatus.BAD_REQUEST) : RuntimeException(message)