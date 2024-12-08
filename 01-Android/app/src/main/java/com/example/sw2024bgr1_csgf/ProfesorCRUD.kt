package com.example.sw2024bgr1_csgf

import java.time.LocalDate

object ProfesorCRUD {
    private const val FILE_NAME = "profesores.txt"

    // Crear un nuevo profesor y asociarlo a la facultad
    fun crearProfesor(id: Int, nombre: String, apellido: String, fechaNacimiento: LocalDate, activo: Boolean, facultadId: Int) {
        val nuevoProfesor = Profesor(id, nombre, apellido, fechaNacimiento, activo, facultadId)

        // Guardar el nuevo profesor en el archivo
        val profesores = FileManager.readFromFile(FILE_NAME).toMutableList()
        profesores.add("${nuevoProfesor.id}|${nuevoProfesor.nombre}|${nuevoProfesor.apellido}|${nuevoProfesor.fechaNacimiento}|${nuevoProfesor.activo}|${nuevoProfesor.facultadId}")
        FileManager.saveToFile(FILE_NAME, profesores)

        println("Profesor creado y asociado a la facultad $facultadId exitosamente.")
    }

    // Leer los profesores desde el archivo
    fun leerProfesores(): List<Profesor> {
        val profesoresString = FileManager.readFromFile(FILE_NAME)
        return profesoresString.mapNotNull {
            val partes = it.split("|")
            if (partes.size == 6) {
                try {
                    val id = partes[0].toInt()
                    val nombre = partes[1]
                    val apellido = partes[2]
                    val fechaNacimiento = LocalDate.parse(partes[3])
                    val activo = partes[4].toBoolean()
                    val facultadId = partes[5].toInt()

                    Profesor(id, nombre, apellido, fechaNacimiento, activo, facultadId)
                } catch (e: Exception) {
                    println("Error al leer el profesor: ${e.message}")
                    null // Si ocurre un error, se ignora esa línea
                }
            } else {
                println("Línea no válida: $it") // Mensaje para líneas mal formadas
                null
            }
        }
    }

    // Actualizar un profesor
    fun actualizarProfesor(id: Int, nuevoNombre: String, nuevoApellido: String, nuevaFechaNacimiento: LocalDate, nuevoActivo: Boolean) {
        val profesores = FileManager.readFromFile(FILE_NAME).toMutableList()
        val profesorIndex = profesores.indexOfFirst { it.split("|")[0].toInt() == id }

        if (profesorIndex != -1) {
            // Obtener los datos actuales y modificarlos según los nuevos valores
            val profesorActual = profesores[profesorIndex].split("|")
            val actualizado = "$id|$nuevoNombre|$nuevoApellido|${nuevaFechaNacimiento}|$nuevoActivo|${profesorActual[5]}"

            // Reemplazar la línea correspondiente
            profesores[profesorIndex] = actualizado
            FileManager.saveToFile(FILE_NAME, profesores)
            println("Profesor actualizado exitosamente.")
        } else {
            println("Profesor no encontrado.")
        }
    }

    // Eliminar un profesor
    fun eliminarProfesor(id: Int) {
        val profesores = FileManager.readFromFile(FILE_NAME).toMutableList()
        val profesorIndex = profesores.indexOfFirst { it.split("|")[0].toInt() == id }

        if (profesorIndex != -1) {
            profesores.removeAt(profesorIndex)
            FileManager.saveToFile(FILE_NAME, profesores)
            println("Profesor eliminado exitosamente.")
        } else {
            println("Profesor no encontrado.")
        }
    }
}