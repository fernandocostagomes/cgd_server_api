package fernandocostagomes.models

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

@Serializable
data class ParameterCgd(val code_parameter_cgd: Int, val name_parameter_cgd: String, val value_parameter_cgd: String)
class ParameterCgdService(private val connection: Connection) {
    companion object {
        private const val TABLE = "parameter_cgd"
        private const val COLUMN_ID = "id_parameter_cgd"
        private const val COLUMN_CODE = "code_parameter_cgd"
        private const val COLUMN_NAME = "name_parameter_cgd"
        private const val COLUMN_VALUE = "value_parameter_cgd"

        private const val CREATE_TABLE_PARAMETER_CDG =
                "CREATE TABLE IF NOT EXISTS " +
                        "$TABLE (" +
                        "$COLUMN_ID SERIAL PRIMARY KEY, " +
                        "$COLUMN_CODE INTEGER NOT NULL, " +
                        "$COLUMN_NAME VARCHAR(20), " +
                        "$COLUMN_VALUE VARCHAR(20));"

        private const val SELECT_PARAMETER_CDG_BY_ID = "SELECT " +
                "$COLUMN_CODE, " +
                "$COLUMN_NAME, " +
                "$COLUMN_VALUE FROM $TABLE WHERE $COLUMN_ID = ?;"

        private const val INSERT_PARAMETER_CDG = "INSERT INTO $TABLE (" +
                "$COLUMN_CODE, " +
                "$COLUMN_NAME, " +
                "$COLUMN_VALUE) VALUES (?, ?, ?);"

        private const val UPDATE_PARAMETER_CDG = "UPDATE $TABLE SET " +
                "$COLUMN_CODE = ?," +
                "$COLUMN_NAME = ?, " +
                "$COLUMN_VALUE = ? " +
                "WHERE $COLUMN_ID = ?;"

        private const val DELETE_PARAMETER_CDG = "DELETE FROM $TABLE WHERE $COLUMN_ID = ?;"
    }

    init {
        try {
            val statement = connection.createStatement()
            statement.executeUpdate(CREATE_TABLE_PARAMETER_CDG)
        } catch (e: SQLException) {
            System.out.println(e.toString())
        }
    }

    private var newParameterCgd_cgdId = 0

    // Create new parameter_cgd
    suspend fun create(parameter_cgd: ParameterCgd): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_PARAMETER_CDG, Statement.RETURN_GENERATED_KEYS)
        statement.setInt(1, parameter_cgd.code_parameter_cgd)
        statement.setString(1, parameter_cgd.name_parameter_cgd)
        statement.setString(2, parameter_cgd.value_parameter_cgd)
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted parameter_cgd")
        }
    }

    // Read a parameter_cgd
    suspend fun read(id: Int): ParameterCgd = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_PARAMETER_CDG_BY_ID)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val code = resultSet.getInt("code_parameter_cgd")
            val name = resultSet.getString("name_parameter_cgd")
            val value = resultSet.getString("value_parameter_cgd")
            return@withContext ParameterCgd(code, name, value)
        } else {
            throw Exception("Record not found")
        }
    }

    // Update a parameter_cgd
    suspend fun update(id: Int, parameter_cgd: ParameterCgd) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_PARAMETER_CDG)
        statement.setInt(1, parameter_cgd.code_parameter_cgd)
        statement.setString(1, parameter_cgd.name_parameter_cgd)
        statement.setString(2, parameter_cgd.value_parameter_cgd)
        statement.executeUpdate()
    }

    // Delete a parameter_cgd
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_PARAMETER_CDG)
        statement.setInt(1, id)
        statement.executeUpdate()
    }
}