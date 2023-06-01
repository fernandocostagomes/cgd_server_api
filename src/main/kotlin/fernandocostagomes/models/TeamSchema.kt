package fernandocostagomes.models

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

@Serializable
data class TeamCgd(val name_team_cgd: String, val pwd_team_cgd: String, val date_team_cgd: String, val id_player: Int)
class TeamCgdService(private val connection: Connection) {
    companion object {
        private const val TABLE = "team_cgd"
        private const val COLUMN_ID = "id_team_cgd"
        private const val COLUMN_NAME = "name_team_cgd"
        private const val COLUMN_PWD = "pwd_team_cgd"
        private const val COLUMN_DATE = "date_team_cgd"
        private const val COLUMN_ID_PLAYER = "id_player"

        private const val CREATE_TABLE_TEAM_CGD = "CREATE TABLE IF NOT EXISTS " +
                "$TABLE (" +
                "$COLUMN_ID SERIAL PRIMARY KEY, " +
                "$COLUMN_NAME VARCHAR(20), " +
                "$COLUMN_PWD VARCHAR(8) NOT NULL, " +
                "$COLUMN_DATE VARCHAR(16)NOT NULL, " +
                "$COLUMN_ID_PLAYER INTEGER)"

        private const val SELECT_TEAM_CGD_BY_ID = "SELECT " +
                "$COLUMN_NAME, " +
                "$COLUMN_PWD, " +
                "$COLUMN_DATE, " +
                "$COLUMN_ID_PLAYER " +
                "FROM $TABLE WHERE $COLUMN_ID = ?"

        private const val INSERT_TEAM_CGD = "INSERT INTO $TABLE (" +
                "$COLUMN_NAME, " +
                "$COLUMN_PWD, " +
                "$COLUMN_DATE, " +
                "$COLUMN_ID_PLAYER) VALUES (?, ?, ?, ?)"

        private const val UPDATE_TEAM_CGD = "UPDATE $TABLE SET " +
                "$COLUMN_NAME = ?," +
                "$COLUMN_PWD = ?," +
                "$COLUMN_DATE = ? WHERE $COLUMN_ID = ?"

        private const val DELETE_TEAM_CGD = "DELETE FROM $TABLE WHERE $COLUMN_ID = ?"

        private const val LIST_TEAM_CGD = "SELECT * FROM $TABLE"
    }

    init {
        try {
            val statement = connection.createStatement()
            statement.executeUpdate(CREATE_TABLE_TEAM_CGD)
        } catch (e: SQLException) {
            println(e.toString())
        }
    }

    private var newTeamCgdId = 0

    // Create new team_cgd
    suspend fun create(team_cgd: TeamCgd): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_TEAM_CGD, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, team_cgd.name_team_cgd)
        statement.setString(2, team_cgd.pwd_team_cgd)
        statement.setString(3, team_cgd.date_team_cgd)
        statement.setInt(4, team_cgd.id_player)
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted team_cgd")
        }
    }

    // Read a team_cgd
    suspend fun read(id: Int): TeamCgd = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_TEAM_CGD_BY_ID)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val name = resultSet.getString(COLUMN_NAME)
            val pwd = resultSet.getString(COLUMN_PWD)
            val date = resultSet.getString(COLUMN_DATE)
            val idPlayer = resultSet.getInt(COLUMN_ID_PLAYER)
            return@withContext TeamCgd(name, pwd, date, idPlayer)
        } else {
            throw Exception("Record not found")
        }
    }

    // Update a team_cgd
    suspend fun update(id: Int, team_cgd: TeamCgd) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_TEAM_CGD)
        statement.setInt(0, id)
        statement.setString(1, team_cgd.name_team_cgd)
        statement.setString(2, team_cgd.pwd_team_cgd)
        statement.setString(2, team_cgd.date_team_cgd)
        statement.setInt(3, team_cgd.id_player)
        statement.executeUpdate()
    }

    // Delete a team_cgd
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_TEAM_CGD)
        statement.setInt(1, id)
        statement.executeUpdate()
    }

    suspend fun list(): List<TeamCgd> = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(LIST_TEAM_CGD)
        val resultSet = statement.executeQuery()

        val team_cgdList = mutableListOf<TeamCgd>()

        while (resultSet.next()) {
            val name = resultSet.getString(COLUMN_NAME)
            val pwd = resultSet.getString(COLUMN_PWD)
            val date = resultSet.getString(COLUMN_DATE)
            val idPlayer = resultSet.getInt(COLUMN_ID_PLAYER)

            val team_cgd = TeamCgd(name, pwd, date, idPlayer)
            team_cgdList.add(team_cgd)
        }

        if (team_cgdList.isNotEmpty()) {
            return@withContext team_cgdList
        } else {
            throw Exception("No records found")
        }
    }
}