package fernandocostagomes.routes

import fernandocostagomes.models.Parameter
import fernandocostagomes.models.ParameterService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRoutingParameter(parameterService: ParameterService){
    routing {
        // Create parameter
        post("/parameter") {
            val parameter = call.receive<Parameter>()
            val id = parameterService.create(parameter)
            call.respond(HttpStatusCode.Created, id)
        }
        // Read parameter
        get("/parameter/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            try {
                val parameter = parameterService.read(id)
                call.respond(HttpStatusCode.OK, parameter)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update parameter
        put("/parameter/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val parameter = call.receive<Parameter>()
            parameterService.update(id, parameter)
            call.respond(HttpStatusCode.OK)
        }
        // Delete parameter
        delete("/parameter/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            parameterService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}