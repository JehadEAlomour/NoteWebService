package com.example.repository

import com.example.data.model.NoteModel
import com.example.data.table.NoteTable
import com.example.data.table.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val database = Database.connect(hikari())
        transaction {
            SchemaUtils.create(UserTable)
            SchemaUtils.create(NoteTable)
            // Add the new column to the existing table

                if (!NoteTable.columns.any { it.name == "COLOR" }) {
                    // Execute an SQL ALTER TABLE statement to add the new column
                    exec("ALTER TABLE ${NoteTable.tableName} ADD COLUMN COLOR VARCHAR(50000)")
                }
                if(!NoteTable.columns.any{ it.name=="IMAGE" }){
                    exec("ALTER TABLE ${NoteTable.tableName} ADD COLUMN IMAGE VARCHAR(50000)")

                }
        }
    }

    fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = System.getenv("JDBC_DRIVER")
        config.jdbcUrl = System.getenv("DATABASE_URL")
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()

        return HikariDataSource(config)

    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }

}