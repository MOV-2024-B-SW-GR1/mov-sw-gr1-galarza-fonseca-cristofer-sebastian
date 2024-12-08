package com.example.sw2024bgr1_csgf

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object FacultadCRUD {
    private const val FILE_NAME = "facultades.txt"

    // Crear una nueva facultad
    fun crearFacultad(id: Int, nombre: String, ubicacion: String, fechaFundacion: LocalDate, presupuesto: Double) {
        val nuevaFacultad = Facultad(id, nombre, ubicacion, fechaFundacion, presupuesto, mutableListOf())

        // Guardar la nueva facultad en el archivo
        val facultades = FileManager.readFromFile(FILE_NAME).toMutableList()
        facultades.add("${nuevaFacultad.id}|${nuevaFacultad.nombre}|${nuevaFacultad.ubicacion}|${nuevaFacultad.fechaFundacion}|${nuevaFacultad.presupuesto}")
        FileManager.saveToFile(FILE_NAME, facultades)

        println("Facultad creada exitosamente.")
    }

    // Leer todas las facultades desde el archivo
    fun leerFacultades(): List<Facultad> {
        val facultadesString = FileManager.readFromFile(FILE_NAME)
        return facultadesString.mapNotNull {
            val partes = it.split("|")
            if (partes.size == 5) {  // Verificar si hay 5 elementos para la facultad
                try {
                    val id = partes[0].toInt()
                    val nombre = partes[1]
                    val ubicacion = partes[2]
                    val fechaFundacion = LocalDate.parse(partes[3])
                    val presupuesto = partes[4].toDouble()

                    Facultad(id, nombre, ubicacion, fechaFundacion, presupuesto)
                } catch (e: Exception) {
                    null // Si hay un error al parsear, ignorarlo
                }
            } else null
        }
    }

    // Actualizar facultad
    fun actualizarFacultad(id: Int, nuevoNombre: String, nuevaUbicacion: String, nuevaFechaFundacion: LocalDate, nuevoPresupuesto: Double) {
        val facultades = FileManager.readFromFile(FILE_NAME).toMutableList()
        val facultadIndex = facultades.indexOfFirst { it.split("|")[0].toInt() == id }

        if (facultadIndex != -1) {
            val partes = facultades[facultadIndex].split("|")
            if (partes.size == 5) {
                val actualizado = "$id|$nuevoNombre|$nuevaUbicacion|$nuevaFechaFundacion|$nuevoPresupuesto"
                facultades[facultadIndex] = actualizado
                FileManager.saveToFile(FILE_NAME, facultades)
                println("Facultad actualizada exitosamente.")
            } else {
                println("Error: El formato de la facultad en el archivo no es correcto.")
            }
        } else {
            println("Facultad no encontrada.")
        }
    }

    // Leer una facultad por ID
    fun leerFacultadPorId(id: Int): Facultad? {
        val facultades = leerFacultades()
        return facultades.find { it.id == id }
    }



    // Eliminar una facultad
    fun eliminarFacultad(id: Int) {
        val facultades = FileManager.readFromFile(FILE_NAME).toMutableList()
        val facultadIndex = facultades.indexOfFirst { it.split("|")[0].toInt() == id }

        if (facultadIndex != -1) {
            facultades.removeAt(facultadIndex)
            FileManager.saveToFile(FILE_NAME, facultades)
            println("Facultad eliminada exitosamente.")
        } else {
            println("Facultad no encontrada.")
        }
    }

    // Guardar la lista de facultades
    fun guardarFacultades(facultades: List<Facultad>) {
        val facultadesString = facultades.map {
            "${it.id}|${it.nombre}|${it.ubicacion}|${it.fechaFundacion}|${it.presupuesto}|${it.profesores.joinToString(",") { it.id.toString() }}"
        }
        FileManager.saveToFile(FILE_NAME, facultadesString)
    }
}