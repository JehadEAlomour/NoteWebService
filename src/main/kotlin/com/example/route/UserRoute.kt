package com.example.route

import com.example.authintoction.JwtService
import com.example.data.model.UserModel
import com.example.data.model.request.LoginRequest
import com.example.data.model.request.UserRequest
import com.example.data.model.response.ResponseModel
import com.example.repository.Repository
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val API_VERSION = "/v1"
const val USERS = "$API_VERSION/users"
const val REGISTER_REQUEST = "$USERS/register"
const val LOGIN_REQUEST = "$USERS/login"
const val ALL_USER_REQUEST = "$USERS/`getAllUser"

@OptIn(KtorExperimentalLocationsAPI::class)
@Location(REGISTER_REQUEST)
class UserRegisterRout


@OptIn(KtorExperimentalLocationsAPI::class)
@Location(REGISTER_REQUEST)
class AllUserRegisterRout

@OptIn(KtorExperimentalLocationsAPI::class)
@Location(LOGIN_REQUEST)
class UserLoginRoute

fun Route.UserRoutes(
    db: Repository,
    jwtService: JwtService,
    hashFunction: (String) -> String
) {
    post<UserRegisterRout>(REGISTER_REQUEST) {
        val registerRequest = try {
            call.receive<UserRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ResponseModel(status = "400", message = e.localizedMessage))
            return@post
        }
        try {
            val user = UserModel(
                userName = registerRequest.name,
                hashPassword = hashFunction(registerRequest.password),
                email = registerRequest.email
            )
            db.addUser(user)
            call.respond(HttpStatusCode.OK, ResponseModel(status = "200", message = jwtService.generateToken(user)))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ResponseModel(status = "400", message = e.localizedMessage))

        }

    }

    post<UserLoginRoute>(LOGIN_REQUEST) {
        val loginRequest = try {
            call.receive<LoginRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ResponseModel(status = "400", message = e.localizedMessage))
            return@post
        }
        try {

            val user = db.getUserByEmail(loginRequest.email)
            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, ResponseModel(status = "400", message = "wrong Email"))
            } else {
                if (user.hashPassword == hashFunction(loginRequest.password)) {
                    call.respond(
                        HttpStatusCode.OK,
                        ResponseModel(status = "0", message = jwtService.generateToken(user))
                    )
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ResponseModel(status = "400", message = "Password isn't Correct")
                    )
                }
            }


        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.Conflict,
                ResponseModel(
                    status = HttpStatusCode.Conflict.toString(),
                    message = e.localizedMessage ?: "Something error"
                )
            )
        }
    }

    get(ALL_USER_REQUEST) {
        try {
            val users = db.getAllUsers()
            call.respond(users!!)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ResponseModel(status = "400", message = e.localizedMessage))

        }
    }
}

