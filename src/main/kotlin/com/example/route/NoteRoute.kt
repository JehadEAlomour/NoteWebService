package com.example.route

import com.example.data.model.NoteModel
import com.example.data.model.request.NoteRequest
import com.example.data.model.response.ResponseModel
import com.example.repository.Repository
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val NOTES = "$API_VERSION/notes"
const val CREATE_NOTE_REQUEST = "$NOTES/create"
const val GET_NOTE_REQUEST = "$NOTES/getNote"
const val GET_ALL_NOTE_REQUEST = "$NOTES/getAllNote"
const val EMAIL_PARAMETER = "EMAIL"


@OptIn(KtorExperimentalLocationsAPI::class)
@Location(GET_NOTE_REQUEST)
class GetNoteRequest

@OptIn(KtorExperimentalLocationsAPI::class)
@Location(CREATE_NOTE_REQUEST)
class CreateNoteRequest

@OptIn(KtorExperimentalLocationsAPI::class)
@Location(GET_ALL_NOTE_REQUEST)
class GetAllNote

fun Route.NoteRoute(
    db: Repository,
) {
    post<CreateNoteRequest>(CREATE_NOTE_REQUEST) {
        val createRequest = try {
            call.receive<NoteRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ResponseModel(status = "400", message = e.localizedMessage))
            return@post
        }
        try {
            val note = NoteModel(
                email = createRequest.email,
                title = createRequest.title,
                body = createRequest.body,
                color = createRequest.color,
                image = createRequest.image

            )
            db.addNote(note)
            call.respond(HttpStatusCode.OK, ResponseModel(status = "200", message = "Save Successfully"))

        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ResponseModel(status = "400", message = e.localizedMessage))

        }

    }

    get(GET_NOTE_REQUEST) {
        try {
            val email = call.request.queryParameters[EMAIL_PARAMETER]
            val notes = db.getNoteByEmail(email!!)
            call.respond(notes!!)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                ResponseModel(status = "400", message = e.localizedMessage.toString())
            )

        }
    }

    get(GET_ALL_NOTE_REQUEST) {
        try {
            val notes = db.getAllNote()
            call.respond(notes!!)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ResponseModel(status = "400", message = e.localizedMessage))

        }
    }

}

