package dasturlash.homework2

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
                return UserDTO(username = user.username, email = user.email, isCorporate = user.isCorporate)
            }
        }
        throw UserNotFoundException("User not found with id $userId")
    }

    override fun getAll(): MutableList<UserDTO> {
        var users : MutableList<UserDTO> = mutableListOf()
        DataBase.users.forEach { user ->
            users.add(UserDTO(username = user.username, email = user.email, isCorporate = user.isCorporate))
        }
        return users
    }

}
fun checkUserAccountCount(userId: Long): Int {
        return DataBase.accounts.count { it.userId == userId }
    }
fun checkUserCorporate(userId: Long): Boolean {
    val account = DataBase.users.find { it.id == userId }
    return account?.isCorporate ?: false
}

// UserService on top

//Account service below
interface AccountService {
    fun create(userId: Long): Any
    fun getBalance(accountId: Long): Any
}

@Service
class AccountServiceImpl(private val userService: UserService) : AccountService {
    override fun create(userId: Long): Any {
        userService.getById(userId)
        val accountCount = checkUserAccountCount(userId)
        val userCorporate = checkUserCorporate(userId)

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
}
//Account Service on top

object DataBase{
    val users: MutableList<UserEntity> = mutableListOf()
    val accounts: MutableList<AccountEntity> = mutableListOf()
    val transactions: MutableList<UserDTO> = mutableListOf()

    fun<T> generationId(list: MutableList<T>) : Long = list.size.toLong()+1
}
