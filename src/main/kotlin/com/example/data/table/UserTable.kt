package com.example.data.table

import org.jetbrains.exposed.sql.Table

object UserTable : Table() {
    val email = varchar(name = "EMAIL", length = 512)
    val name = varchar(name = "USER_NAME", length = 512)
    val password = varchar(name = "USER_PASSWORD", length = 512)
    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(email)
}