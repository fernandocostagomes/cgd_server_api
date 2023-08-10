package fernandocostagomes.models.vmais

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

@Serializable
data class Address(
    val nameAddress: String,
    val codeAddress: Int,
    val addressAddress: String,
    val numberAddress: String,
    val cityAddress: String,
    val stateAddress: String)
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
                "$COLUMN_NAME, " +
                "$COLUMN_CODE, " +
                "$COLUMN_ADDRESS," +
                "$COLUMN_NUMBER," +
                "$COLUMN_CITY," +
                "$COLUMN_STATE FROM $TABLE WHERE $COLUMN_ID = ?;"

        private const val INSERT_ADDRESS = "INSERT INTO $TABLE (" +
                "$COLUMN_NAME, " +
                "$COLUMN_CODE, " +
                "$COLUMN_ADDRESS," +
                "$COLUMN_NUMBER," +
                "$COLUMN_CITY," +
                "$COLUMN_STATE) VALUES (?, ?, ?, ?, ?, ?);"

        private const val UPDATE_ADDRESS = "UPDATE $TABLE SET " +
                "$COLUMN_NAME = ?," +
                "$COLUMN_CODE = ?, " +
                "$COLUMN_ADDRESS = ? " +
                "$COLUMN_NUMBER = ? " +
                "$COLUMN_CITY = ? " +
                "$COLUMN_STATE = ? " +
                "WHERE $COLUMN_ID = ?;"

        private const val DELETE_ADDRESS = "DELETE FROM $TABLE WHERE $COLUMN_ID = ?;"
    }

    init {
        try {
            val statement = connection.createStatement()
            statement.executeUpdate(CREATE_TABLE_ADDRESS)
        } catch (e: SQLException) {
            println(e.toString())
        }
    }

    private var newAddressId = 0

    // Create new address
    suspend fun create(address: Address): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_ADDRESS, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, address.nameAddress)
        statement.setInt(2, address.codeAddress)
        statement.setString(3, address.addressAddress)
        statement.setString(4, address.numberAddress)
        statement.setString(5, address.cityAddress)
        statement.setString(6, address.stateAddress)
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
            val name = resultSet.getString("name_address")
            val code = resultSet.getInt("code_address")
            val address = resultSet.getString("address_address")
            val number = resultSet.getString("number_address")
            val city = resultSet.getString("city_address")
            val state = resultSet.getString("state_address")
            return@withContext Address(name, code, address, number, city, state)
        } else {
            throw Exception("Record not found")
        }
    }

    // Update a address
    suspend fun update(id: Int, address: Address) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_ADDRESS)
        statement.setInt(0, id)
        statement.setString(1, address.nameAddress)
        statement.setInt(2, address.codeAddress)
        statement.setString(3, address.addressAddress)
        statement.setString(4, address.numberAddress)
        statement.setString(5, address.cityAddress)
        statement.setString(6, address.stateAddress)
        statement.executeUpdate()
    }

    // Delete a address
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_ADDRESS)
        statement.setInt(1, id)
        statement.executeUpdate()
    }
}