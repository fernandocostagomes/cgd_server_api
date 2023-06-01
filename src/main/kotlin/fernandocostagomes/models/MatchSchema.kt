package fernandocostagomes.models

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

@Serializable
data class MatchCgd(val date_match_cgd: String,
                 val winner_1_id_player: Int,
                 val winner_2_id_player: Int,
                 val lose_1_id_player: Int,
                 val lose_2_id_player: Int,
                 val qtd_match_cgd_winner: Int,
                 val qtd_match_cgd_lose: Int)
class MatchCgdService(private val connection: Connection) {
    companion object {
        private const val TABLE = "match_cgd"
        private const val COLUMN_ID = "id_match_cgd"
        private const val COLUMN_DATE = "date_match_cgd"
        private const val COLUMN_WINNER_1 = "winner_1_id_player"
        private const val COLUMN_WINNER_2 = "winner_2_id_player"
        private const val COLUMN_LOSE_1 = "lose_1_id_player"
        private const val COLUMN_LOSE_2 = "lose_2_id_player"
        private const val COLUMN_QTD_MATCH_CGD_WINNER = "qtd_match_cgd_winner"
        private const val COLUMN_QTD_MATCH_CGD_LOSE = "qtd_match_cgd_lose"

        private const val CREATE_TABLE_MATCH_CGD = "CREATE TABLE IF NOT EXISTS " +
                "$TABLE (" +
                "$COLUMN_ID SERIAL PRIMARY KEY, " +
                "$COLUMN_DATE VARCHAR(20), " +
                "$COLUMN_WINNER_1 INTEGER NOT NULL, " +
                "$COLUMN_WINNER_2 INTEGER, " +
                "$COLUMN_LOSE_1 INTEGER NOT NULL, " +
                "$COLUMN_LOSE_2 INTEGER, " +
                "$COLUMN_QTD_MATCH_CGD_WINNER INTEGER NOT NULL, " +
                "$COLUMN_QTD_MATCH_CGD_LOSE INTEGER NOT NULL)"

        private const val SELECT_MATCH_CGD_BY_ID = "SELECT " +
                "$COLUMN_DATE, " +
                "$COLUMN_WINNER_1, " +
                "$COLUMN_WINNER_2, " +
                "$COLUMN_LOSE_1, " +
                "$COLUMN_LOSE_2, " +
                "$COLUMN_QTD_MATCH_CGD_WINNER, " +
                "$COLUMN_QTD_MATCH_CGD_LOSE " +
                "FROM $TABLE WHERE $COLUMN_ID = ?"

        private const val INSERT_MATCH_CGD = "INSERT INTO $TABLE (" +
                "$COLUMN_DATE, " +
                "$COLUMN_WINNER_1, " +
                "$COLUMN_WINNER_2, " +
                "$COLUMN_LOSE_1, " +
                "$COLUMN_LOSE_2, " +
                "$COLUMN_QTD_MATCH_CGD_WINNER, " +
                "$COLUMN_QTD_MATCH_CGD_LOSE) VALUES (?, ?, ?, ?, ?, ?, ?)"

        private const val UPDATE_MATCH_CGD = "UPDATE $TABLE SET " +
                "$COLUMN_DATE = ?," +
                "$COLUMN_WINNER_1 = ?," +
                "$COLUMN_WINNER_2 = ?," +
                "$COLUMN_LOSE_1 = ?," +
                "$COLUMN_LOSE_2 = ?," +
                "$COLUMN_QTD_MATCH_CGD_WINNER = ?," +
                "$COLUMN_QTD_MATCH_CGD_LOSE = ? WHERE $COLUMN_ID = ?"

        private const val DELETE_MATCH_CGD = "DELETE FROM $TABLE WHERE $COLUMN_ID = ?"

        private const val LIST_MATCH_CGD = "SELECT * FROM $TABLE"
    }

    init {
        try {
            val statement = connection.createStatement()
            statement.executeUpdate(CREATE_TABLE_MATCH_CGD)
        } catch (e: SQLException) {
            println(e.toString())
        }
    }

    private var newMatchCgdId = 0

    // Create new match_cgd
    suspend fun create(match_cgd: MatchCgd): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_MATCH_CGD, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, match_cgd.date_match_cgd)
        statement.setInt(2, match_cgd.winner_1_id_player)
        statement.setInt(3, match_cgd.winner_2_id_player)
        statement.setInt(4, match_cgd.lose_1_id_player)
        statement.setInt(5, match_cgd.lose_2_id_player)
        statement.setInt(6, match_cgd.qtd_match_cgd_winner)
        statement.setInt(7, match_cgd.qtd_match_cgd_lose)
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted match_cgd")
        }
    }

    // Read a match_cgd
    suspend fun read(id: Int): MatchCgd = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_MATCH_CGD_BY_ID)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val date_match_cgd = resultSet.getString(COLUMN_DATE)
            val winner_1_id_player = resultSet.getInt(COLUMN_WINNER_1)
            val winner_2_id_player = resultSet.getInt(COLUMN_WINNER_2)
            val lose_1_id_player = resultSet.getInt(COLUMN_LOSE_1)
            val lose_2_id_player = resultSet.getInt(COLUMN_LOSE_2)
            val qtd_match_cgd_winner = resultSet.getInt(COLUMN_QTD_MATCH_CGD_WINNER)
            val qtd_match_cgd_lose = resultSet.getInt(COLUMN_QTD_MATCH_CGD_LOSE)
            return@withContext MatchCgd(
                date_match_cgd,
                winner_1_id_player,
                winner_2_id_player,
                lose_1_id_player,
                lose_2_id_player,
                qtd_match_cgd_winner,
                qtd_match_cgd_lose)
        } else {
            throw Exception("Record not found")
        }
    }

    // Update a match_cgd
    suspend fun update(id: Int, match_cgd: MatchCgd) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_MATCH_CGD)
        statement.setInt(0, id)
        statement.setString(1, match_cgd.date_match_cgd)
        statement.setInt(2, match_cgd.winner_1_id_player)
        statement.setInt(3, match_cgd.winner_2_id_player)
        statement.setInt(4, match_cgd.lose_1_id_player)
        statement.setInt(5, match_cgd.lose_2_id_player)
        statement.setInt(6, match_cgd.qtd_match_cgd_winner)
        statement.setInt(7, match_cgd.qtd_match_cgd_lose)
        statement.executeUpdate()
    }

    // Delete a match_cgd
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_MATCH_CGD)
        statement.setInt(1, id)
        statement.executeUpdate()
    }

    suspend fun list(): List<MatchCgd> = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(LIST_MATCH_CGD)
        val resultSet = statement.executeQuery()

        val match_cgdList = mutableListOf<MatchCgd>()

        while (resultSet.next()) {
            val date_match_cgd = resultSet.getString(COLUMN_DATE)
            val winner_1_id_player = resultSet.getInt(COLUMN_WINNER_1)
            val winner_2_id_player = resultSet.getInt(COLUMN_WINNER_2)
            val lose_1_id_player = resultSet.getInt(COLUMN_LOSE_1)
            val lose_2_id_player = resultSet.getInt(COLUMN_LOSE_2)
            val qtd_match_cgd_winner = resultSet.getInt(COLUMN_QTD_MATCH_CGD_WINNER)
            val qtd_match_cgd_lose = resultSet.getInt(COLUMN_QTD_MATCH_CGD_LOSE)

            val match_cgd = MatchCgd(
                date_match_cgd,
                winner_1_id_player,
                winner_2_id_player,
                lose_1_id_player,
                lose_2_id_player,
                qtd_match_cgd_winner,
                qtd_match_cgd_lose)
            match_cgdList.add(match_cgd)
        }

        if (match_cgdList.isNotEmpty()) {
            return@withContext match_cgdList
        } else {
            throw Exception("No records found")
        }
    }
}