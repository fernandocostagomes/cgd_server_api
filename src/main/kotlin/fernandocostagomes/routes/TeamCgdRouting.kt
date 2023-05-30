package fernandocostagomes.routes

import fernandocostagomes.models.TeamCgd
import fernandocostagomes.models.TeamCgdService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRoutingTeam(teamCgdService: TeamCgdService){
    routing {
        // Create team_cgd
        post("/team_cgd") {
            val team_cgd = call.receive<TeamCgd>()
            val id = teamCgdService.create(team_cgd)
            call.respond(HttpStatusCode.Created, id)
        }
        // List all teans
        get("/team_cgd") {
            val listTeam = teamCgdService.list()
            call.respond(HttpStatusCode.OK, listTeam)
        }
        // Read team_cgd
        get("/team_cgd/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            try {
                val team_cgd = teamCgdService.read(id)
                call.respond(HttpStatusCode.OK, team_cgd)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update team_cgd
        put("/team_cgd/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val team_cgd = call.receive<TeamCgd>()
            teamCgdService.update(id, team_cgd)
            call.respond(HttpStatusCode.OK)
        }
        // Delete team_cgd
        delete("/team_cgd/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            teamCgdService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}