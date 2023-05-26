package fernandocostagomes.routes

import fernandocostagomes.models.Team
import fernandocostagomes.models.TeamService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRoutingTeam(teamService: TeamService){
    routing {
        // Create team
        post("/team") {
            val team = call.receive<Team>()
            val id = teamService.create(team)
            call.respond(HttpStatusCode.Created, id)
        }
        // Read team
        get("/team/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            try {
                val team = teamService.read(id)
                call.respond(HttpStatusCode.OK, team)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update team
        put("/team/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val team = call.receive<Team>()
            teamService.update(id, team)
            call.respond(HttpStatusCode.OK)
        }
        // Delete team
        delete("/team/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            teamService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}