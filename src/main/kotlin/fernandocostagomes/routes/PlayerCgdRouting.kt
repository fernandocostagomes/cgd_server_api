package fernandocostagomes.routes

import fernandocostagomes.models.PlayerCgd
import fernandocostagomes.models.PlayerCgdService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRoutingPlayer(player_cgdService: PlayerCgdService){
    routing {
        // Create player_cgd
        post("/player_cgd") {
            val player_cgd = call.receive<PlayerCgd>()
            val id = player_cgdService.create(player_cgd)
            call.respond(HttpStatusCode.Created, id)
        }
        // List all player_cgd
        get("/player_cgd") {
            val listTeam = player_cgdService.list()
            call.respond(HttpStatusCode.OK, listTeam)
        }
        // Read player_cgd
        get("/player_cgd/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            try {
                val player_cgd = player_cgdService.read(id)
                call.respond(HttpStatusCode.OK, player_cgd)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update player_cgd
        put("/player_cgd/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val player_cgd = call.receive<PlayerCgd>()
            player_cgdService.update(id, player_cgd)
            call.respond(HttpStatusCode.OK)
        }
        // Delete player_cgd
        delete("/player_cgd/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            player_cgdService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}