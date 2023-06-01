package fernandocostagomes.models

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

@Serializable
data class PlayerCgd(val name_player_cgd: String, val phone_player_cgd: String, val email_player_cgd: String, val date_player_cgd: String)
class PlayerCgdService(private val connection: Connection) {
    companion object {
        private const val TABLE = "player_cgd"
        private const val COLUMN_ID = "id_player_cgd"
        private const val COLUMN_NAME = "name_player_cgd"
        private const val COLUMN_PHONE = "phone_player_cgd"
        private const val COLUMN_EMAIL = "email_player_cgd"
        private const val COLUMN_DATE = "date_player_cgd"

        private const val CREATE_TABLE_PLAYER_CGD = "CREATE TABLE IF NOT EXISTS " +
                "$TABLE (" +
                "$COLUMN_ID SERIAL PRIMARY KEY, " +
                "$COLUMN_NAME VARCHAR(20), " +
                "$COLUMN_PHONE VARCHAR(11) NOT NULL, " +
                "$COLUMN_EMAIL VARCHAR(50) NOT NULL, " +
                "$COLUMN_DATE VARCHAR(16)NOT NULL)"

        private const val SELECT_PLAYER_CGD_BY_ID = "SELECT " +
                "$COLUMN_NAME, " +
                "$COLUMN_PHONE, " +
                "$COLUMN_EMAIL, " +
                "$COLUMN_DATE, " +
                "FROM $TABLE WHERE $COLUMN_ID = ?"

        private const val INSERT_PLAYER_CGD = "INSERT INTO $TABLE (" +
                "$COLUMN_NAME, " +
                "$COLUMN_PHONE, " +
                "$COLUMN_EMAIL, " +
                "$COLUMN_DATE " +
                "VALUES (?, ?, ?, ?)"

        private const val UPDATE_PLAYER_CGD = "UPDATE $TABLE SET " +
                "$COLUMN_NAME = ?," +
                "$COLUMN_PHONE = ?," +
                "$COLUMN_EMAIL = ?," +
                "$COLUMN_DATE WHERE $COLUMN_ID = ?"

        private const val DELETE_PLAYER_CGD = "DELETE FROM $TABLE WHERE $COLUMN_ID = ?"

        private const val LIST_PLAYER_CGD = "SELECT * FROM $TABLE"
    }

    init {
        try {
            val statement = connection.createStatement()
            statement.executeUpdate(CREATE_TABLE_PLAYER_CGD)
        } catch (e: SQLException) {
            println(e.toString())
        }
    }

    private var newPlayerCgdId = 0

    // Create new player_cgd
    suspend fun create(player_cgd: PlayerCgd): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_PLAYER_CGD, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, player_cgd.name_player_cgd)
        statement.setString(2, player_cgd.phone_player_cgd)
        statement.setString(3, player_cgd.email_player_cgd)
        statement.setString(4, player_cgd.date_player_cgd)
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted player_cgd")
        }
    }

    // Read a player_cgd
    suspend fun read(id: Int): PlayerCgd = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_PLAYER_CGD_BY_ID)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val name = resultSet.getString(COLUMN_NAME)
            val phone = resultSet.getString(COLUMN_PHONE)
            val email = resultSet.getString(COLUMN_EMAIL)
            val date = resultSet.getString(COLUMN_DATE)
            return@withContext PlayerCgd(name, phone, email,date)
        } else {
            throw Exception("Record not found")
        }
    }

    // Update a player_cgd
    suspend fun update(id: Int, player_cgd: PlayerCgd) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_PLAYER_CGD)
        statement.setInt(0, id)
        statement.setString(1, player_cgd.name_player_cgd)
        statement.setString(2, player_cgd.phone_player_cgd)
        statement.setString(2, player_cgd.email_player_cgd)
        statement.setString(3, player_cgd.date_player_cgd)
        statement.executeUpdate()
    }

    // Delete a player_cgd
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_PLAYER_CGD)
        statement.setInt(1, id)
        statement.executeUpdate()
    }

    suspend fun list(): List<PlayerCgd> = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(PlayerCgdService.LIST_PLAYER_CGD)
        val resultSet = statement.executeQuery()

        val player_cgdList = mutableListOf<PlayerCgd>()

        while (resultSet.next()) {
            val name = resultSet.getString(PlayerCgdService.COLUMN_NAME)
            val phone = resultSet.getString(PlayerCgdService.COLUMN_PHONE)
            val email = resultSet.getString(PlayerCgdService.COLUMN_EMAIL)
            val date = resultSet.getString(PlayerCgdService.COLUMN_DATE)

            val player_cgd = PlayerCgd(name, phone, email, date)
            player_cgdList.add(player_cgd)
        }

        if (player_cgdList.isNotEmpty()) {
            return@withContext player_cgdList
        } else {
            throw Exception("No records found")
        }
    }
}