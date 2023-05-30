package fernandocostagomes.models

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

@Serializable
data class UserCgd(val date_user_cgd: String,
                val email_user_cgd: String,
                val phone_user_cgd: String,
                val pwd_user_cgd: String)
class UserCgdService(private val connection: Connection) {
    companion object {
        private const val TABLE = "user_cgd"
        private const val COLUMN_ID = "id_user_cgd"
        private const val COLUMN_DATE = "date_user_cgd"
        private const val COLUMN_EMAIL = "email_user_cgd"
        private const val COLUMN_PHONE = "phone_user_cgd"
        private const val COLUMN_PWD = "pwd_user_cgd"

        private const val CREATE_TABLE_USER_CGD =
            "CREATE TABLE IF NOT EXISTS $TABLE(" +
                "$COLUMN_ID SERIAL PRIMARY KEY, " +
                "$COLUMN_DATE VARCHAR(20), " +
                "$COLUMN_EMAIL VARCHAR(50) NOT NULL, " +
                "$COLUMN_PHONE VARCHAR(11) NOT NULL, " +
                "$COLUMN_PWD VARCHAR(8) NOT NULL);"

        private const val SELECT_USER_CGD_BY_ID = "SELECT " +
                "$COLUMN_DATE, " +
                "$COLUMN_EMAIL, " +
                "$COLUMN_PHONE, " +
                "$COLUMN_PWD " +
                "FROM $TABLE WHERE $COLUMN_ID = ?"

        private const val INSERT_USER_CGD = "INSERT INTO $TABLE (" +
                "$COLUMN_DATE, " +
                "$COLUMN_EMAIL, " +
                "$COLUMN_PHONE, " +
                "$COLUMN_PWD) " +
                "VALUES (?, ?, ?, ?)"

        private const val UPDATE_USER_CGD = "UPDATE $TABLE SET " +
                "$COLUMN_DATE = ?," +
                "$COLUMN_EMAIL = ?," +
                "$COLUMN_PHONE = ?," +
                "$COLUMN_PWD = ? WHERE $COLUMN_ID = ?"

        private const val DELETE_USER_CGD = "DELETE FROM $TABLE WHERE $COLUMN_ID = ?"

        private const val LIST_USER_CGD = "SELECT * FROM $TABLE"
    }

    init {
        try {
            val statement = connection.createStatement()
            statement.executeUpdate(CREATE_TABLE_USER_CGD)
        } catch (e: SQLException) {
            println(e.toString())
        }
    }

    private var newUserCgdId = 0

    // Create new user_cgd
    suspend fun create(user_cgd: UserCgd): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_USER_CGD, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, user_cgd.date_user_cgd)
        statement.setString(2, user_cgd.email_user_cgd)
        statement.setString(3, user_cgd.phone_user_cgd)
        statement.setString(4, user_cgd.pwd_user_cgd)
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted user_cgd")
        }
    }

    // Read a user_cgd
    suspend fun read(id: Int): UserCgd = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_USER_CGD_BY_ID)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val date_user_cgd = resultSet.getString(COLUMN_DATE)
            val email_user_cgd = resultSet.getString(COLUMN_EMAIL)
            val phone_user_cgd = resultSet.getString(COLUMN_PHONE)
            val password_user_cgd = resultSet.getString(COLUMN_PWD)

            return@withContext UserCgd(
                date_user_cgd,
                email_user_cgd,
                phone_user_cgd,
                password_user_cgd)
        } else {
            throw Exception("Record not found")
        }
    }

    // Update a user_cgd
    suspend fun update(id: Int, user_cgd: UserCgd) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_USER_CGD)
        statement.setString(1, user_cgd.date_user_cgd)
        statement.setString(2, user_cgd.email_user_cgd)
        statement.setString(3, user_cgd.phone_user_cgd)
        statement.setString(4, user_cgd.pwd_user_cgd)
        statement.executeUpdate()
    }

    // Delete a user_cgd
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_USER_CGD)
        statement.setInt(1, id)
        statement.executeUpdate()
    }

    suspend fun list(): List<UserCgd> = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(LIST_USER_CGD)
        val resultSet = statement.executeQuery()

        val user_cgdList = mutableListOf<UserCgd>()

        while (resultSet.next()) {
            val date_user_cgd = resultSet.getString(COLUMN_DATE)
            val email_user_cgd = resultSet.getString(COLUMN_EMAIL)
            val phone_user_cgd = resultSet.getString(COLUMN_PHONE)
            val pwd_user_cgd = resultSet.getString(COLUMN_PWD)

            val user_cgd = UserCgd(
                date_user_cgd,
                email_user_cgd,
                phone_user_cgd,
                pwd_user_cgd)
            user_cgdList.add(user_cgd)
        }

        if (user_cgdList.isNotEmpty()) {
            return@withContext user_cgdList
        } else {
            throw Exception("No records found")
        }
    }
}