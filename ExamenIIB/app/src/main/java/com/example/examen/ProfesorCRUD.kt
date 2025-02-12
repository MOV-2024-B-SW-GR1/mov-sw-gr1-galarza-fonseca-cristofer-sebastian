package com.example.examen

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import java.time.LocalDate

object ProfesorCRUD {
    private val db: SQLiteDatabase
        get() = DatabaseManager.getInstance().writableDatabase

    fun crearProfesor(id: Int, nombre: String, apellido: String, fechaNacimiento: LocalDate, activo: Boolean, facultadId: Int) {
        val values = ContentValues().apply {
            put("id", id)
            put("nombre", nombre)
            put("apellido", apellido)
            put("fecha_nacimiento", fechaNacimiento.toString())
            put("activo", if (activo) 1 else 0)
            put("facultad_id", facultadId)
        }
        db.insert("profesores", null, values)
    }

    fun leerProfesores(): List<Profesor> {
        val profesores = mutableListOf<Profesor>()
        val cursor = db.query("profesores", null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            profesores.add(
                Profesor(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                    fechaNacimiento = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("fecha_nacimiento"))),
                    activo = cursor.getInt(cursor.getColumnIndexOrThrow("activo")) == 1,
                    facultadId = cursor.getInt(cursor.getColumnIndexOrThrow("facultad_id"))
                )
            )
        }
        cursor.close()
        return profesores
    }

    fun actualizarProfesor(id: Int, nuevoNombre: String, nuevoApellido: String, nuevaFechaNacimiento: LocalDate, nuevoActivo: Boolean) {
        val values = ContentValues().apply {
            put("nombre", nuevoNombre)
            put("apellido", nuevoApellido)
            put("fecha_nacimiento", nuevaFechaNacimiento.toString())
            put("activo", if (nuevoActivo) 1 else 0)
        }

        db.update("profesores", values, "id = ?", arrayOf(id.toString()))
    }

    fun eliminarProfesor(id: Int) {
        db.delete("profesores", "id = ?", arrayOf(id.toString()))
    }

    fun leerProfesorPorId(id: Int): Profesor? {
        val cursor = db.query(
            "profesores",
            null,
            "id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val profesor = Profesor(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                fechaNacimiento = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("fecha_nacimiento"))),
                activo = cursor.getInt(cursor.getColumnIndexOrThrow("activo")) == 1,
                facultadId = cursor.getInt(cursor.getColumnIndexOrThrow("facultad_id"))
            )
            cursor.close()
            profesor
        } else {
            cursor.close()
            null
        }
    }
}