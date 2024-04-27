package com.example.repository

import com.example.data.model.NoteModel
import com.example.data.model.UserModel
import com.example.data.table.NoteTable
import com.example.data.table.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*

class Repository {
    //---USER---
    suspend fun addUser(user: UserModel) {
        //this how insert data in database
        dbQuery {
            UserTable.insert { row ->
                row[UserTable.email] = user.email
                row[UserTable.name] = user.userName
                row[UserTable.password] = user.hashPassword
            }
        }
    }

    suspend fun getUserByEmail(email: String): UserModel? = dbQuery {
        val user = UserTable.select {
            UserTable.email.eq(email)
        }.map {
            rowToUsers(it)
        }.singleOrNull()

        return@dbQuery user
    }

    private fun rowToUser(row: ResultRow?): UserModel? {
        //this fun to convert user from ResultRow to UserModel
        if (row != null) {
            return UserModel(
                email = row[UserTable.email], userName = row[UserTable.name], hashPassword = row[UserTable.password]
            )
        } else {
            return null
        }
    }

    private fun rowToUsers(row: ResultRow): UserModel? {
        val email = row[UserTable.email] ?: return null
        val userName = row[UserTable.name] ?: return null
        val hashPassword = row[UserTable.password] ?: return null

        return UserModel(
            email = email,
            userName = userName,
            hashPassword = hashPassword
        )
    }

    suspend fun getAllUsers(): List<UserModel?>? {
        try {
            return dbQuery {
                UserTable.selectAll().map { rowToUser(it) }
            }
        } catch (e: Exception) {
            return null
        }
    }

    //---NOTE---

    suspend fun addNote(note: NoteModel) {
        dbQuery {
            NoteTable.insert { row ->
                row[NoteTable.email] = note.email
                row[NoteTable.body] = note.body
                row[NoteTable.title] = note.title
                row[NoteTable.image]=note.image
                row[NoteTable.color]=note.color
            }
        }
    }

    suspend fun getNoteByEmail(email: String): List<NoteModel?>? {
        try {
            return dbQuery {
                NoteTable.selectAll().where { NoteTable.email.eq(email) }
                    .map { rowToNote(it) }
            }

        } catch (e: Exception) {
            return null
        }
    }

    suspend fun getAllNote(): List<NoteModel?>? {
        try {
            return dbQuery {
                NoteTable.selectAll()
                    .map { rowToNote(it) }
            }

        } catch (e: Exception) {
            return null
        }
    }
}

private fun rowToNote(resultRow: ResultRow): NoteModel? {
    if (resultRow != null) {
        return NoteModel(
            email = resultRow[NoteTable.email], title = resultRow[NoteTable.title],
            body = resultRow[NoteTable.body],
            color = resultRow[NoteTable.color],
            image = resultRow[NoteTable.image]


        )
    } else {
        return null

    }
}


