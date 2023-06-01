package fernandocostagomes

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import fernandocostagomes.plugins.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    //configureSecurity()
    configureHTTP()
    configureSerialization()
    configureDatabases()
    configureRouting()
}
