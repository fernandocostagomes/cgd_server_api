package fernandocostagomes.models

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement
import java.util.*

@Serializable
data class Team( val name_team: String, val pwd_team: String, val date_team: String, val id_player: Int)
class TeamService(private val connection: Connection) {
    companion object {
        private const val TABLE = "team"
        private const val COLUMN_ID = "id_team"
        private const val COLUMN_NAME = "name_team"
        private const val COLUMN_PWD = "pwd_team"
        private const val COLUMN_DATE = "date_team"
        private const val COLUMN_ID_PLAYER = "id_player"

        private const val CREATE_TABLE_TEAM = "CREATE TABLE IF NOT EXISTS " +
                "$TABLE (" +
                "$COLUMN_ID SERIAL PRIMARY KEY, " +
                "$COLUMN_NAME VARCHAR(20), " +
                "$COLUMN_PWD VARCHAR(8) NOT NULL, " +
                "$COLUMN_DATE VARCHAR(16)NOT NULL, " +
                "$COLUMN_ID_PLAYER INTEGER)"

        private const val SELECT_TEAM_BY_ID = "SELECT " +
                "$COLUMN_NAME, " +
                "$COLUMN_PWD, " +
                "$COLUMN_DATE, " +
                "$COLUMN_ID_PLAYER, " +
                "FROM $TABLE WHERE $COLUMN_ID = ?"

        private const val INSERT_TEAM = "INSERT INTO $TABLE (" +
                "$COLUMN_NAME, " +
                "$COLUMN_PWD, " +
                "$COLUMN_DATE, " +
                "$COLUMN_ID_PLAYER) VALUES (?, ?, ?, ?)"

        private const val UPDATE_TEAM = "UPDATE $TABLE SET " +
                "$COLUMN_NAME = ?," +
                "$COLUMN_PWD = ?," +
                "$COLUMN_DATE = ?," +
                "$COLUMN_ID_PLAYER WHERE $COLUMN_ID = ?"

        private const val DELETE_TEAM = "DELETE FROM $TABLE WHERE $COLUMN_ID = ?"
    }

    init {
        try {
            val statement = connection.createStatement()
            statement.executeUpdate(CREATE_TABLE_TEAM)
        } catch (e: SQLException) {
            println(e.toString())
        }
    }

    private var newTeamId = 0

    // Create new team
    suspend fun create(team: Team): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_TEAM, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, team.name_team)
        statement.setString(2, team.pwd_team)
        statement.setString(3, team.date_team)
        statement.setInt(4, team.id_player)
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted team")
        }
    }

    // Read a team
    suspend fun read(id: Int): Team = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_TEAM_BY_ID)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val name = resultSet.getString(COLUMN_NAME)
            val pwd = resultSet.getString(COLUMN_PWD)
            val date = resultSet.getString(COLUMN_DATE)
            val idPlayer = resultSet.getInt(COLUMN_ID_PLAYER)
            return@withContext Team(name, pwd, date, idPlayer)
        } else {
            throw Exception("Record not found")
        }
    }

    // Update a team
    suspend fun update(id: Int, team: Team) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_TEAM)
        statement.setString(1, team.name_team)
        statement.setString(2, team.pwd_team)
        statement.setString(2, team.date_team)
        statement.setInt(3, team.id_player)
        statement.executeUpdate()
    }

    // Delete a team
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_TEAM)
        statement.setInt(1, id)
        statement.executeUpdate()
    }
}