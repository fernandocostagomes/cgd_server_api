package fernandocostagomes.routes

import fernandocostagomes.models.vmais.Address
import fernandocostagomes.models.vmais.AddressService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRoutingAddress(addressService: AddressService){
    routing {
        // Create address
        post("/address") {
            val address = call.receive<Address>()
            val id = addressService.create(address)
            call.respond(HttpStatusCode.Created, id)
        }
        // List all address
//        get("/address") {
//            val listAddress = addressService.read()
//            call.respond(HttpStatusCode.OK, listAddress)
//        }
        // Read address
        get("/address/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            try {
                val address = addressService.read(id)
                call.respond(HttpStatusCode.OK, address)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update address
        put("/address/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val address = call.receive<Address>()
            addressService.update(id, address)
            call.respond(HttpStatusCode.OK)
        }
        // Delete address
        delete("/address/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            addressService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}