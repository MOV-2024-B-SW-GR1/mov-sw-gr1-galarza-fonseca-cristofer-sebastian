package com.example.sw2024bgr1_csgf

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import java.time.LocalDate


object FacultadCRUD {

    fun crearFacultad(id: Int, nombre: String, ubicacion: String, fechaFundacion: LocalDate, presupuesto: Double) {
        DatabaseManager.getConnection().use { conn ->
            val sql = """
                INSERT INTO facultades (id, nombre, ubicacion, fecha_fundacion, presupuesto)
                VALUES (?, ?, ?, ?, ?)
            """.trimIndent()

            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                stmt.setString(2, nombre)
                stmt.setString(3, ubicacion)
                stmt.setString(4, fechaFundacion.toString())
                stmt.setDouble(5, presupuesto)
                stmt.executeUpdate()
            }
        }
        println("Facultad creada exitosamente.")
    }

    fun leerFacultades(): List<Facultad> {
        val facultades = mutableListOf<Facultad>()
        DatabaseManager.getConnection().use { conn ->
            val sql = "SELECT * FROM facultades"
            conn.createStatement().use { stmt ->
                val rs = stmt.executeQuery(sql)
                while (rs.next()) {
                    facultades.add(
                        Facultad(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("ubicacion"),
                            LocalDate.parse(rs.getString("fecha_fundacion")),
                            rs.getDouble("presupuesto")
                        )
                    )
                }
            }
        }
        return facultades
    }

    fun actualizarFacultad(id: Int, nuevoNombre: String, nuevaUbicacion: String, nuevaFechaFundacion: LocalDate, nuevoPresupuesto: Double) {
        DatabaseManager.getConnection().use { conn ->
            val sql = """
                UPDATE facultades 
                SET nombre = ?, ubicacion = ?, fecha_fundacion = ?, presupuesto = ?
                WHERE id = ?
            """.trimIndent()

            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, nuevoNombre)
                stmt.setString(2, nuevaUbicacion)
                stmt.setString(3, nuevaFechaFundacion.toString())
                stmt.setDouble(4, nuevoPresupuesto)
                stmt.setInt(5, id)

                val rowsAffected = stmt.executeUpdate()
                if (rowsAffected > 0) {
                    println("Facultad actualizada exitosamente.")
                } else {
                    println("No se encontró la facultad con ID: $id")
                }
            }
        }
    }

    fun eliminarFacultad(id: Int) {
        DatabaseManager.getConnection().use { conn ->
            val sql = "DELETE FROM facultades WHERE id = ?"
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                val rowsAffected = stmt.executeUpdate()
                if (rowsAffected > 0) {
                    println("Facultad eliminada exitosamente.")
                } else {
                    println("No se encontró la facultad con ID: $id")
                }
            }
        }
    }

    fun leerFacultadPorId(id: Int): Facultad? {
        DatabaseManager.getConnection().use { conn ->
            val sql = "SELECT * FROM facultades WHERE id = ?"
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return Facultad(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("ubicacion"),
                        LocalDate.parse(rs.getString("fecha_fundacion")),
                        rs.getDouble("presupuesto")
                    )
                }
            }
        }
        return null
    }
}