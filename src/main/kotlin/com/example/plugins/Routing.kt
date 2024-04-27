package com.example.plugins

import com.example.authintoction.JwtService
import com.example.authintoction.hash
import com.example.data.model.UserModel
import com.example.data.model.response.ResponseModel
import com.example.repository.Repository
import com.example.route.LOGIN_REQUEST
import com.example.route.NoteRoute
import com.example.route.REGISTER_REQUEST
import com.example.route.UserRoutes
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.style

fun Application.configureRouting() {
    install(DoubleReceive)
    val db = Repository()
    val jwtService = JwtService()
    val hashFunction = { s: String -> hash(s) }



    routing {
        get("/") {
            call.respondHtml {
                body {
                    style {
                        +"body { background-color: #0099ff; }"
                        +"div.centered-text { position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); color: white; font-weight: bold; text-align: center; }" // Centered text style
                    }
                    div(classes = "centered-text") {
                        +"WEB SERVICE NOTE \n 25/4/2024"
                    }
                }
            }


        }

        get("/token") {
            val email = call.request.queryParameters["EMAIL"]!!
            val password = call.request.queryParameters["PASSWORD"]!!
            val userName = call.request.queryParameters["USER_NAME"]!!

            val user = UserModel(
                userName = userName,
                hashPassword = hashFunction(password),
                email = email
            )
            call.respond(jwtService.generateToken(user))
        }

        UserRoutes(db, jwtService, hashFunction)

        NoteRoute(db)

    }

}


