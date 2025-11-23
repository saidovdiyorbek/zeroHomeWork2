package dasturlash.homework2

import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.nio.file.attribute.UserPrincipalNotFoundException
import java.time.LocalDateTime
import javax.xml.crypto.Data
//User service below
interface UserService {
    fun create(user: UserDTO): Any
    fun checkUsernameIsExists(username: String): Boolean
    fun getById(userId: Long): UserDTO
    fun getAll(): MutableList<UserDTO>
}

@Service
class UserServiceImpl : UserService {
    override fun create(user: UserDTO): Any {
        val isExists = checkUsernameIsExists(user.username)
        if (isExists) {
            return "User already exists"
        }
        var newUser = UserEntity(id = DataBase.generationId(DataBase.users), username = user.username, email = user.email, isCorporate = user.isCorporate, createdDate = LocalDateTime.now())
        DataBase.users.add(newUser)
        return "User created!"
    }

    override fun checkUsernameIsExists(username: String): Boolean {
        for (user in DataBase.users) {
            if (user.username == username){
                return true
            }
        }
        return false
    }

    override fun getById(userId: Long): UserDTO {
        DataBase.users.forEach { user ->
            if (user.id == userId){
                return UserDTO(id = user.id, username = user.username, email = user.email, isCorporate = user.isCorporate)
            }
        }
        throw UserNotFoundException("User not found with id $userId")
    }

    override fun getAll(): MutableList<UserDTO> {
        var users : MutableList<UserDTO> = mutableListOf()
        DataBase.users.forEach { user ->
            users.add(UserDTO(id = user.id, username = user.username, email = user.email, isCorporate = user.isCorporate))
        }
        return users
    }
    fun checkUserAccountCount(userId: Long): Int {
        return DataBase.accounts.count { it.userId == userId }
    }
    fun checkUserCorporate(userId: Long): Boolean {
        val account = DataBase.users.find { it.id == userId }
        return account?.isCorporate ?: false
    }
}
// UserService on top

//Account service below
interface AccountService {
    fun create(userId: Long): Any
    fun getBalance(accountId: Long): Any
}

@Service
class AccountServiceImpl(private val userService: UserServiceImpl) : AccountService {
    override fun create(userId: Long): Any {
        userService.getById(userId)
        val accountCount = checkUserAccountCount(userId)
        val userCorporate = userService.checkUserCorporate(userId)

        if ((accountCount == 5 && !userCorporate) || accountCount == 10){
            if (userCorporate){
                throw UserLimitIsOverException()
            }
            throw UserNotCorporateException("You are not corporate your limit 5")

        }
        DataBase.accounts.add(AccountEntity(id = DataBase.generationId(DataBase.accounts), userId = userId))
        return "Account created!"
    }

    override fun getBalance(accountId: Long): Any {
        val find = DataBase.accounts.find { it.id == accountId }
        return find?.balance ?: "Account not found"
    }

    fun checkUserAccountCount(userId: Long): Int {
        return DataBase.accounts.count { it.userId == userId }
    }

    fun findById(accountId: Long): AccountEntity? {
        return DataBase.accounts.find { it.id == accountId }
    }
}
//Account Service on top


//Transaction Service below

interface TransactionService {
    fun deposit(transaction: TransactionDTO): Any
    fun withdraw(accountId: Long, amount: Double): Any
    fun transfer(transfer: TransactionDTO.TransferDTO): Any

}

@Service
class TransactionImpl(
    private val accountServiceImpl: AccountServiceImpl,
    private val userService: UserServiceImpl,
) : TransactionService {
    override fun deposit(transaction: TransactionDTO): Any {
        var account = accountServiceImpl.findById(transaction.toAccountId)     ?: throw AccountNotFoundException()
        if(transaction.amount <= 0.0)  throw InvalidAmountException()
        DataBase.transactions.add(TransactionEntity(id = DataBase.generationId(DataBase.transactions), "SYSTEM", transaction.toAccountId, transaction.amount))
        account.balance += transaction.amount
        return "Transaction deposited!"
    }

    override fun withdraw(accountId: Long, amount: Double): Any {
        val account = accountServiceImpl.findById(accountId) ?: throw AccountNotFoundException()
        if(amount <= 0.0 || amount > account.balance) throw InvalidAmountException()
        DataBase.transactions.add(TransactionEntity(id = DataBase.generationId(DataBase.transactions), account.id, "SYSTEM", amount))
        account.balance -= amount
        return "Transaction withdraw!"
    }

    override fun transfer(transfer: TransactionDTO.TransferDTO): Any {
        var fromAccount = accountServiceImpl.findById(transfer.fromAccountId) ?: throw AccountNotFoundException()
        var toAccount = accountServiceImpl.findById(transfer.toAccountId) ?: throw AccountNotFoundException()

        val fromUser = userService.checkUserCorporate(fromAccount.userId)
        val toUser = userService.checkUserCorporate(toAccount.userId)

        var checkAmount = transfer.amount
        if (toUser) {
            if(!fromUser) {
                checkAmount = transfer.amount + transfer.amount * 1.5 / 100;
            }
        }


        if (transfer.fromAccountId == transfer.toAccountId) throw SameAccountTransactionException()
        if (fromAccount.balance > checkAmount && checkAmount > 0){
            DataBase.transactions.add(TransactionEntity(id = DataBase.generationId(DataBase.transactions), fromAccountId = transfer.fromAccountId, toAccountId = transfer.toAccountId, amount = checkAmount))
            fromAccount.balance -= checkAmount
            toAccount.balance += transfer.amount
            return "Transaction transfer!"
        }
        throw BadRequestException("Not enough balance or amount is negative")
    }

}
@Component
object DataBase{
    val users: MutableList<UserEntity> = mutableListOf()
    val accounts: MutableList<AccountEntity> = mutableListOf()
    val transactions: MutableList<TransactionEntity> = mutableListOf()


    fun defaultEntities(){
        users.add(UserEntity(1, "Diyor", "diyor@gmail.com", true, LocalDateTime.now()))
        users.add(UserEntity(2, "Oybek", "oybek@gmail.com", true, LocalDateTime.now()))

        accounts.add(AccountEntity(1, 1, 25000.0, LocalDateTime.now()))
        accounts.add(AccountEntity(2, 2, 0.0, LocalDateTime.now()))
    }
    fun<T> generationId(list: MutableList<T>) : Long = list.size.toLong()+1
}
