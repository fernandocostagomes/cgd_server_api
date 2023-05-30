package fernandocostagomes.routes

import fernandocostagomes.models.ParameterCgd
import fernandocostagomes.models.ParameterCgdService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRoutingParameter(parameterCgdService: ParameterCgdService){
    routing {
        // Create parameter_cgd
        post("/parameter_cgd") {
            val parameter_cgd = call.receive<ParameterCgd>()
            val id = parameterCgdService.create(parameter_cgd)
            call.respond(HttpStatusCode.Created, id)
        }
        // Read parameter_cgd
        get("/parameter_cgd/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            try {
                val parameter_cgd = parameterCgdService.read(id)
                call.respond(HttpStatusCode.OK, parameter_cgd)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update parameter_cgd
        put("/parameter_cgd/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val parameter_cgd = call.receive<ParameterCgd>()
            parameterCgdService.update(id, parameter_cgd)
            call.respond(HttpStatusCode.OK)
        }
        // Delete parameter_cgd
        delete("/parameter_cgd/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            parameterCgdService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}