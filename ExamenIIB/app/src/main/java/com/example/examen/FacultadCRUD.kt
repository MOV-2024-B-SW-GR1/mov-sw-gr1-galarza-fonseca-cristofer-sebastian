package com.example.examen

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import java.time.LocalDate

object FacultadCRUD {
    private val db: SQLiteDatabase
        get() = DatabaseManager.getInstance().writableDatabase

    fun crearFacultad(id: Int, nombre: String, ubicacion: String, fechaFundacion: LocalDate, presupuesto: Double, latitud: Double?, longitud: Double?) {
        val values = ContentValues().apply {
            put("id", id)
            put("nombre", nombre)
            put("ubicacion", ubicacion)
            put("fecha_fundacion", fechaFundacion.toString())
            put("presupuesto", presupuesto)
            put("latitud", latitud)
            put("longitud", longitud)
        }
        db.insert("facultades", null, values)
    }

    fun leerFacultades(): List<Facultad> {
        val facultades = mutableListOf<Facultad>()
        val cursor = db.query("facultades", null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            facultades.add(
                Facultad(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    ubicacion = cursor.getString(cursor.getColumnIndexOrThrow("ubicacion")),
                    fechaFundacion = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("fecha_fundacion"))),
                    presupuesto = cursor.getDouble(cursor.getColumnIndexOrThrow("presupuesto")),
                    latitud = cursor.getDouble(cursor.getColumnIndexOrThrow("latitud")),
                    longitud = cursor.getDouble(cursor.getColumnIndexOrThrow("longitud"))
                )
            )
        }
        cursor.close()
        return facultades
    }

    fun actualizarFacultad(id: Int, nuevoNombre: String, nuevaUbicacion: String, nuevaFechaFundacion: LocalDate, nuevoPresupuesto: Double, nuevaLatitud: Double?, nuevaLongitud: Double?) {
        val values = ContentValues().apply {
            put("nombre", nuevoNombre)
            put("ubicacion", nuevaUbicacion)
            put("fecha_fundacion", nuevaFechaFundacion.toString())
            put("presupuesto", nuevoPresupuesto)
            put("latitud", nuevaLatitud)
            put("longitud", nuevaLongitud)
        }

        db.update("facultades", values, "id = ?", arrayOf(id.toString()))
    }

    fun eliminarFacultad(id: Int) {
        db.delete("facultades", "id = ?", arrayOf(id.toString()))
    }

    fun leerFacultadPorId(id: Int): Facultad? {
        val cursor = db.query(
            "facultades",
            null,
            "id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val facultad = Facultad(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                ubicacion = cursor.getString(cursor.getColumnIndexOrThrow("ubicacion")),
                fechaFundacion = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("fecha_fundacion"))),
                presupuesto = cursor.getDouble(cursor.getColumnIndexOrThrow("presupuesto")),
                latitud = cursor.getDouble(cursor.getColumnIndexOrThrow("latitud")),
                longitud = cursor.getDouble(cursor.getColumnIndexOrThrow("longitud"))
            )
            cursor.close()
            facultad
        } else {
            cursor.close()
            null
        }
    }
}