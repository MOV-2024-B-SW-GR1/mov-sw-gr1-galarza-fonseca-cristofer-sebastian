package com.example.sw2024bgr1_csgf

import java.sql.Connection
import java.sql.DriverManager

object DatabaseManager {
    private const val DB_URL = "jdbc:sqlite:universidad.db"
    private var connection: Connection? = null

    init {
        createTables()
    }

    private fun connect(): Connection {
        if (connection == null || connection?.isClosed == true) {
            connection = DriverManager.getConnection(DB_URL)
        }
        return connection!!
    }

    private fun createTables() {
        val connection = connect()
        connection.createStatement().use { statement ->
            // Crear tabla Facultades
            statement.execute("""
                CREATE TABLE IF NOT EXISTS facultades (
                    id INTEGER PRIMARY KEY,
                    nombre TEXT NOT NULL,
                    ubicacion TEXT NOT NULL,
                    fecha_fundacion TEXT NOT NULL,
                    presupuesto REAL NOT NULL
                )
            """)

            // Crear tabla Profesores
            statement.execute("""
                CREATE TABLE IF NOT EXISTS profesores (
                    id INTEGER PRIMARY KEY,
                    nombre TEXT NOT NULL,
                    apellido TEXT NOT NULL,
                    fecha_nacimiento TEXT NOT NULL,
                    activo BOOLEAN NOT NULL,
                    facultad_id INTEGER,
                    FOREIGN KEY (facultad_id) REFERENCES facultades(id)
                )
            """)
        }
    }

    fun getConnection(): Connection = connect()
}