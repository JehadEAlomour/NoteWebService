package com.example.data.table

import org.jetbrains.exposed.sql.Table

object NoteTable : Table() {
    val email = varchar(name = "EMAIL", length = 512)
    val body = varchar(name = "BODY", length = 50000)
    val title = varchar(name = "TITLE", length = 50000)
    val image = varchar(name = "IMAGE", length = 50000)
    val color = varchar(name = "COLOR", length = 50000)

}