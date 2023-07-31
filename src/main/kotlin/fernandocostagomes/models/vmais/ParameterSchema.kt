package fernandocostagomes.models.vmais

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

@Serializable
data class Parameter(val code_parameter: Int, val name_parameter: String, val value_parameter: String)
class ParameterService(private val connection: Connection) {
    companion object {
        private const val TABLE = "parameter"
        private const val COLUMN_ID = "id_parameter_"
        private const val COLUMN_CODE = "code_parameter"
        private const val COLUMN_NAME = "name_parameter"
        private const val COLUMN_VALUE = "value_parameter"

        private const val CREATE_TABLE_PARAMETER =
                "CREATE TABLE IF NOT EXISTS " +
                        "$TABLE (" +
                        "$COLUMN_ID SERIAL PRIMARY KEY, " +
                        "$COLUMN_CODE INTEGER NOT NULL, " +
                        "$COLUMN_NAME VARCHAR(20), " +
                        "$COLUMN_VALUE VARCHAR(20));"

        private const val SELECT_PARAMETER_BY_ID = "SELECT " +
                "$COLUMN_CODE, " +
                "$COLUMN_NAME, " +
                "$COLUMN_VALUE FROM $TABLE WHERE $COLUMN_ID = ?;"

        private const val INSERT_PARAMETER = "INSERT INTO $TABLE (" +
                "$COLUMN_CODE, " +
                "$COLUMN_NAME, " +
                "$COLUMN_VALUE) VALUES (?, ?, ?);"

        private const val UPDATE_PARAMETER = "UPDATE $TABLE SET " +
                "$COLUMN_CODE = ?," +
                "$COLUMN_NAME = ?, " +
                "$COLUMN_VALUE = ? " +
                "WHERE $COLUMN_ID = ?;"

        private const val DELETE_PARAMETER = "DELETE FROM $TABLE WHERE $COLUMN_ID = ?;"
    }

    init {
        try {
            val statement = connection.createStatement()
            statement.executeUpdate(CREATE_TABLE_PARAMETER)
        } catch (e: SQLException) {
            System.out.println(e.toString())
        }
    }

    private var newParameterId = 0

    // Create new parameter
    suspend fun create(parameter: Parameter): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_PARAMETER, Statement.RETURN_GENERATED_KEYS)
        statement.setInt(1, parameter.code_parameter)
        statement.setString(1, parameter.name_parameter)
        statement.setString(2, parameter.value_parameter)
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted parameter")
        }
    }

    // Read a parameter
    suspend fun read(id: Int): Parameter = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_PARAMETER_BY_ID)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val code = resultSet.getInt("code_parameter")
            val name = resultSet.getString("name_parameter")
            val value = resultSet.getString("value_parameter")
            return@withContext Parameter(code, name, value)
        } else {
            throw Exception("Record not found")
        }
    }

    // Update a parameter
    suspend fun update(id: Int, parameter: Parameter) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_PARAMETER)
        statement.setInt(0, id)
        statement.setInt(1, parameter.code_parameter)
        statement.setString(1, parameter.name_parameter)
        statement.setString(2, parameter.value_parameter)
        statement.executeUpdate()
    }

    // Delete a parameter
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_PARAMETER)
        statement.setInt(1, id)
        statement.executeUpdate()
    }
}