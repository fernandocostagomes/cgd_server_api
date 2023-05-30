package fernandocostagomes.routes

import fernandocostagomes.models.MatchCgd
import fernandocostagomes.models.MatchCgdService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRoutingMatchCgd(matchCgdService: MatchCgdService){
    routing {
        // Create matchCgd
        post("/matchCgd") {
            val matchCgd = call.receive<MatchCgd>()
            val id = matchCgdService.create(matchCgd)
            call.respond(HttpStatusCode.Created, id)
        }
        // List all matchCgd
        get("/matchCgd") {
            val listMatchCgd = matchCgdService.list()
            call.respond(HttpStatusCode.OK, listMatchCgd)
        }
        // Read matchCgd
        get("/matchCgd/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            try {
                val matchCgd = matchCgdService.read(id)
                call.respond(HttpStatusCode.OK, matchCgd)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update matchCgd
        put("/matchCgd/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val matchCgd = call.receive<MatchCgd>()
            matchCgdService.update(id, matchCgd)
            call.respond(HttpStatusCode.OK)
        }
        // Delete matchCgd
        delete("/matchCgd/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            matchCgdService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}