package fernandocostagomes.plugins

import fernandocostagomes.models.*
import fernandocostagomes.models.vmais.AddressService
import fernandocostagomes.models.vmais.ParameterService
import fernandocostagomes.models.vmais.UserService
import fernandocostagomes.routes.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.*
import java.sql.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureDatabases() {

    val dbConnection: Connection = connectToPostgres(embedded = true)

    val parameterService = ParameterService(dbConnection)
    val userService = UserService(dbConnection)
    val addressService = AddressService(dbConnection)

    configureRoutingParameter(parameterService)
    configureRoutingUser(userService)
    configureRoutingAddress(addressService)
}

/**
 * Makes a connection to a Postgres database.
 *
 * In order to connect to your running Postgres process,
 * please specify the following parameters in your configuration file:
 * - postgres.url -- Url of your running database process.
 * - postgres.user -- Username for database connection
 * - postgres.password -- Password for database connection
 *
 * If you don't have a database process running yet, you may need to [download]((https://www.postgresql.org/download/))
 * and install Postgres and follow the instructions [here](https://postgresapp.com/).
 * Then, you would be able to edit your url,  which is usually "jdbc:postgresql://host:port/database", as well as
 * user and password values.
 *
 *
 * @param embedded -- if [true] defaults to an embedded database for tests that runs locally in the same process.
 * In this case you don't have to provide any parameters in configuration file, and you don't have to run a process.
 *
 * @return [Connection] that represent connection to the database. Please, don't forget to close this connection when
 * your application shuts down by calling [Connection.close]
 * */
fun Application.connectToPostgres(embedded: Boolean): Connection {

    val url = "15.235.33.242"
    val db = "dbcgd"
    val port = "5432"
    val user = "vmaispostgres"
    val pwd = "senhavmaispostgres"

    Class.forName("org.postgresql.Driver")

    return if (embedded) {
        DriverManager.getConnection("jdbc:postgresql://$url:$port/$db", user, pwd)
    } else {
//        val url = environment.config.property("postgres.url").getString()
//        val user = environment.config.property("postgres.user").getString()
//        val password = environment.config.property("postgres.password").getString()

        DriverManager.getConnection(url, user, pwd)
    }
}

