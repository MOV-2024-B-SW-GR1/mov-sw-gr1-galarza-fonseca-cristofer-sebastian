import com.example.sw2024bgr1_csgf.DatabaseManager
import com.example.sw2024bgr1_csgf.Profesor
import java.time.LocalDate

object ProfesorCRUD {

    fun crearProfesor(id: Int, nombre: String, apellido: String, fechaNacimiento: LocalDate, activo: Boolean, facultadId: Int) {
        DatabaseManager.getConnection().use { conn ->
            val sql = """
                INSERT INTO profesores (id, nombre, apellido, fecha_nacimiento, activo, facultad_id)
                VALUES (?, ?, ?, ?, ?, ?)
            """.trimIndent()

            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                stmt.setString(2, nombre)
                stmt.setString(3, apellido)
                stmt.setString(4, fechaNacimiento.toString())
                stmt.setBoolean(5, activo)
                stmt.setInt(6, facultadId)
                stmt.executeUpdate()
            }
        }
        println("Profesor creado exitosamente.")
    }

    fun leerProfesores(): List<Profesor> {
        val profesores = mutableListOf<Profesor>()
        DatabaseManager.getConnection().use { conn ->
            val sql = "SELECT * FROM profesores"
            conn.createStatement().use { stmt ->
                val rs = stmt.executeQuery(sql)
                while (rs.next()) {
                    profesores.add(
                        Profesor(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            LocalDate.parse(rs.getString("fecha_nacimiento")),
                            rs.getBoolean("activo"),
                            rs.getInt("facultad_id")
                        )
                    )
                }
            }
        }
        return profesores
    }

    fun actualizarProfesor(id: Int, nuevoNombre: String, nuevoApellido: String, nuevaFechaNacimiento: LocalDate, nuevoActivo: Boolean) {
        DatabaseManager.getConnection().use { conn ->
            val sql = """
                UPDATE profesores 
                SET nombre = ?, apellido = ?, fecha_nacimiento = ?, activo = ?
                WHERE id = ?
            """.trimIndent()

            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, nuevoNombre)
                stmt.setString(2, nuevoApellido)
                stmt.setString(3, nuevaFechaNacimiento.toString())
                stmt.setBoolean(4, nuevoActivo)
                stmt.setInt(5, id)

                val rowsAffected = stmt.executeUpdate()
                if (rowsAffected > 0) {
                    println("Profesor actualizado exitosamente.")
                } else {
                    println("No se encontró el profesor con ID: $id")
                }
            }
        }
    }

    fun eliminarProfesor(id: Int) {
        DatabaseManager.getConnection().use { conn ->
            val sql = "DELETE FROM profesores WHERE id = ?"
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                val rowsAffected = stmt.executeUpdate()
                if (rowsAffected > 0) {
                    println("Profesor eliminado exitosamente.")
                } else {
                    println("No se encontró el profesor con ID: $id")
                }
            }
        }
    }

    fun leerProfesorPorId(id: Int): Profesor? {
        DatabaseManager.getConnection().use { conn ->
            val sql = "SELECT * FROM profesores WHERE id = ?"
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return Profesor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        LocalDate.parse(rs.getString("fecha_nacimiento")),
                        rs.getBoolean("activo"),
                        rs.getInt("facultad_id")
                    )
                }
            }
        }
        return null
    }
}