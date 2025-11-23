package dasturlash.homework2

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
){
    @Operation(summary = "Create a new user")
    @PostMapping
    fun create(@RequestBody user: UserDTO) = userService.create(user)

    @Operation(summary = "Get user by id")
    @GetMapping("/{userId}")
    fun getById(@PathVariable userId: Long): UserDTO = userService.getById(userId)

    @Operation(summary = "Get all users")
    @GetMapping
    fun getAll(): MutableList<UserDTO> = userService.getAll()


}

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val accountService: AccountService,
){

    @Operation(summary = "Create account")
    @PostMapping
    fun create(@RequestParam userId: Long): Any = accountService.create(userId)

    @Operation(summary = "Get account balance")
    @GetMapping("/{accountId}")
    fun getBalance(@PathVariable accountId: Long): Any = accountService.getBalance(accountId)
}

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val transactionService: TransactionService,
){
    @Operation(summary = "Deposit transaction")
    @PostMapping("/deposit")
    fun deposit(@RequestBody transaction: TransactionDTO) = transactionService.deposit(transaction)

    @Operation(summary = "Withdraw transaction amount")
    @PostMapping("/withdraw/{accountId}")
    fun withdraw(@PathVariable accountId: Long,
                 @RequestParam amount: Double) = transactionService.withdraw(accountId, amount)

    @Operation(summary = "Transfer transaction between accounts")
    @PostMapping("/transfer")
    fun transfer(@RequestBody transfer: TransactionDTO.TransferDTO) = transactionService.transfer(transfer)
}

@RestController
@RequestMapping("/api/users")
class DefaultEntity(
    private val dataBase: DataBase
){

    @PostMapping("/default-entities")
    fun defaultEntities() = dataBase.defaultEntities()
}
