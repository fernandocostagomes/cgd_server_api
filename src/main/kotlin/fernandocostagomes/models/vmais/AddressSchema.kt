package fernandocostagomes.models.vmais

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

@Serializable
data class Address(val code_address: Int, val name_address: String, val value_address: String)
class AddressService(private val connection: Connection) {
    companion object {
        private const val TABLE = "address"
        private const val COLUMN_ID = "address_id"
        private const val COLUMN_NAME = "address_name"
        private const val COLUMN_CODE = "address_code"
        private const val COLUMN_ADDRESS = "address_address"
        private const val COLUMN_NUMBER = "address_number"
        private const val COLUMN_CITY = "address_city"
        private const val COLUMN_STATE = "address_state"

        private const val CREATE_TABLE_ADDRESS =
                "CREATE TABLE IF NOT EXISTS " +
                        "$TABLE (" +
                        "$COLUMN_ID SERIAL PRIMARY KEY, " +
                        "$COLUMN_NAME VARCHAR(20), " +
                        "$COLUMN_CODE INTEGER NOT NULL, " +
                        "$COLUMN_ADDRESS VARCHAR(40)," +
                        "$COLUMN_NUMBER VARCHAR(8)," +
                        "$COLUMN_CITY VARCHAR(30)," +
                        "$COLUMN_STATE VARCHAR(30));"

        private const val SELECT_ADDRESS_BY_ID = "SELECT " +
                "$COLUMN_CODE, " +
                "$COLUMN_NAME, " +
                "$COLUMN_VALUE FROM $TABLE WHERE $COLUMN_ID = ?;"

        private const val INSERT_ADDRESS = "INSERT INTO $TABLE (" +
                "$COLUMN_CODE, " +
                "$COLUMN_NAME, " +
                "$COLUMN_VALUE) VALUES (?, ?, ?);"

        private const val UPDATE_ADDRESS = "UPDATE $TABLE SET " +
                "$COLUMN_CODE = ?," +
                "$COLUMN_NAME = ?, " +
                "$COLUMN_VALUE = ? " +
                "WHERE $COLUMN_ID = ?;"

        private const val DELETE_ADDRESS = "DELETE FROM $TABLE WHERE $COLUMN_ID = ?;"
    }

    init {
        try {
            val statement = connection.createStatement()
            statement.executeUpdate(CREATE_TABLE_ADDRESS)
        } catch (e: SQLException) {
            System.out.println(e.toString())
        }
    }

    private var newAddressId = 0

    // Create new address
    suspend fun create(address: Address): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_ADDRESS, Statement.RETURN_GENERATED_KEYS)
        statement.setInt(1, address.code_address)
        statement.setString(1, address.name_address)
        statement.setString(2, address.value_address)
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted address")
        }
    }

    // Read a address
    suspend fun read(id: Int): Address = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_ADDRESS_BY_ID)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val code = resultSet.getInt("code_address")
            val name = resultSet.getString("name_address")
            val value = resultSet.getString("value_address")
            return@withContext Address(code, name, value)
        } else {
            throw Exception("Record not found")
        }
    }

    // Update a address
    suspend fun update(id: Int, address: Address) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_ADDRESS)
        statement.setInt(0, id)
        statement.setInt(1, address.code_address)
        statement.setString(1, address.name_address)
        statement.setString(2, address.value_address)
        statement.executeUpdate()
    }

    // Delete a address
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_ADDRESS)
        statement.setInt(1, id)
        statement.executeUpdate()
    }
}