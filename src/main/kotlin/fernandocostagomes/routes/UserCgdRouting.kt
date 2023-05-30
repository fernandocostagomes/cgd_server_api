package fernandocostagomes.routes

import fernandocostagomes.models.UserCgd
import fernandocostagomes.models.UserCgdService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRoutingUserCgd(user_cgdService: UserCgdService){
    routing {
        // Create user_cgd
        post("/user_cgd") {
            val user_cgd = call.receive<UserCgd>()
            val id = user_cgdService.create(user_cgd)
            call.respond(HttpStatusCode.Created, id)
        }
        // Read user_cgd
        get("/user_cgd/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            try {
                val user_cgd = user_cgdService.read(id)
                call.respond(HttpStatusCode.OK, user_cgd)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update user_cgd
        put("/user_cgd/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user_cgd = call.receive<UserCgd>()
            user_cgdService.update(id, user_cgd)
            call.respond(HttpStatusCode.OK)
        }
        // Delete user_cgd
        delete("/user_cgd/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            user_cgdService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}