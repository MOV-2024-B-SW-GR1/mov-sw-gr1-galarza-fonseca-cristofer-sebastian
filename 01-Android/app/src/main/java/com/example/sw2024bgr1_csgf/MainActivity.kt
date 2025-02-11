package com.example.sw2024bgr1_csgf

import java.io.File
import java.time.LocalDate

class MainActivity {
    fun iniciar() {
        // Inicializar la base de datos
        DatabaseManager.getConnection()
        println("Base de datos inicializada en: ${File("universidad.db").absolutePath}")

        // Menú principal
        while (true) {
            println("\n=== Menú Principal ===")
            println("1. Gestión de Facultades")
            println("2. Gestión de Profesores")
            println("3. Verificar Base de Datos")
            println("4. Salir")
            print("Elige una opción: ")

            when (readLine()?.toIntOrNull()) {
                1 -> menuFacultades()
                2 -> menuProfesores()
                3 -> verificarBaseDeDatos()
                4 -> {
                    println("Saliendo del programa...")
                    break
                }
                else -> println("Opción no válida. Intenta de nuevo.")
            }
        }
    }

    private fun verificarBaseDeDatos() {
        println("\n=== Estado de la Base de Datos ===")
        DatabaseManager.getConnection().use { conn ->
            // Verificar tabla facultades
            conn.createStatement().use { stmt ->
                println("\nTabla Facultades:")
                val rsFacultades = stmt.executeQuery("SELECT * FROM facultades")
                var countFacultades = 0
                while (rsFacultades.next()) {
                    countFacultades++
                    println("ID: ${rsFacultades.getInt("id")}")
                    println("Nombre: ${rsFacultades.getString("nombre")}")
                    println("Ubicación: ${rsFacultades.getString("ubicacion")}")
                    println("Fecha Fundación: ${rsFacultades.getString("fecha_fundacion")}")
                    println("Presupuesto: ${rsFacultades.getDouble("presupuesto")}")
                    println("-------------------")
                }
                println("Total de facultades: $countFacultades")
            }

            // Verificar tabla profesores
            conn.createStatement().use { stmt ->
                println("\nTabla Profesores:")
                val rsProfesores = stmt.executeQuery("SELECT * FROM profesores")
                var countProfesores = 0
                while (rsProfesores.next()) {
                    countProfesores++
                    println("ID: ${rsProfesores.getInt("id")}")
                    println("Nombre: ${rsProfesores.getString("nombre")}")
                    println("Apellido: ${rsProfesores.getString("apellido")}")
                    println("Fecha Nacimiento: ${rsProfesores.getString("fecha_nacimiento")}")
                    println("Activo: ${rsProfesores.getBoolean("activo")}")
                    println("Facultad ID: ${rsProfesores.getInt("facultad_id")}")
                    println("-------------------")
                }
                println("Total de profesores: $countProfesores")
            }
        }
    }

    private fun menuFacultades() {
        while (true) {
            println("=== GESTIÓN DE FACULTADES ===")
            println("1. Crear Facultad")
            println("2. Leer Facultades")
            println("3. Actualizar Facultad")
            println("4. Eliminar Facultad")
            println("5. Volver al Menú Principal")
            print("Elige una opción: ")

            when (readLine()?.toIntOrNull()) {
                1 -> crearFacultad()
                2 -> mostrarFacultadesConProfesores()
                3 -> actualizarFacultad()
                4 -> eliminarFacultad()
                5 -> return
                else -> println("Opción no válida. Intenta de nuevo.")
            }
        }
    }

    private fun crearFacultad() {
        print("ID de la facultad: ")
        val id = readLine()?.toIntOrNull() ?: return

        print("Nombre de la facultad: ")
        val nombre = readLine() ?: return

        print("Ubicación de la facultad: ")
        val ubicacion = readLine() ?: return

        print("Fecha de fundación de la facultad (yyyy-MM-dd): ")
        val fecha = readLine()?.let { LocalDate.parse(it) } ?: return

        print("Presupuesto de la facultad: ")
        val presupuesto = readLine()?.toDoubleOrNull() ?: return

        try {
            FacultadCRUD.crearFacultad(id, nombre, ubicacion, fecha, presupuesto)
        } catch (e: Exception) {
            println("Error al crear la facultad: ${e.message}")
        }
    }

    private fun mostrarFacultadesConProfesores() {
        val facultades = FacultadCRUD.leerFacultades()
        val profesores = ProfesorCRUD.leerProfesores()

        if (facultades.isEmpty()) {
            println("No hay facultades registradas.")
            return
        }

        for (facultad in facultades) {
            println("\nFacultad ID: ${facultad.id}")
            println("Nombre: ${facultad.nombre}")
            println("Ubicación: ${facultad.ubicacion}")
            println("Fecha de Fundación: ${facultad.fechaFundacion}")
            println("Presupuesto: ${facultad.presupuesto}")
            println("Profesores:")

            val profesoresDeLaFacultad = profesores.filter { it.facultadId == facultad.id }
            if (profesoresDeLaFacultad.isEmpty()) {
                println("  No hay profesores asociados.")
            } else {
                for (profesor in profesoresDeLaFacultad) {
                    println("  - ID: ${profesor.id}, Nombre: ${profesor.nombre} ${profesor.apellido}")
                }
            }
        }
    }

    private fun actualizarFacultad() {
        println("Ingrese el ID de la facultad a actualizar: ")
        val id = readLine()?.toIntOrNull() ?: return

        val facultadExistente = FacultadCRUD.leerFacultadPorId(id)
        if (facultadExistente == null) {
            println("No se encontró la facultad con ID: $id")
            return
        }

        println("Actualizando la facultad ${facultadExistente.nombre}:")
        print("Nuevo nombre (${facultadExistente.nombre}): ")
        val nombre = readLine()?.takeIf { it.isNotBlank() } ?: facultadExistente.nombre

        print("Nueva ubicación (${facultadExistente.ubicacion}): ")
        val ubicacion = readLine()?.takeIf { it.isNotBlank() } ?: facultadExistente.ubicacion

        print("Nueva fecha de fundación (${facultadExistente.fechaFundacion}) [yyyy-MM-dd]: ")
        val fecha = readLine()?.let { LocalDate.parse(it) } ?: facultadExistente.fechaFundacion

        print("Nuevo presupuesto (${facultadExistente.presupuesto}): ")
        val presupuesto = readLine()?.toDoubleOrNull() ?: facultadExistente.presupuesto

        try {
            FacultadCRUD.actualizarFacultad(id, nombre, ubicacion, fecha, presupuesto)
        } catch (e: Exception) {
            println("Error al actualizar la facultad: ${e.message}")
        }
    }

    private fun eliminarFacultad() {
        println("Ingrese el ID de la facultad a eliminar: ")
        val id = readLine()?.toIntOrNull() ?: return

        val facultadExistente = FacultadCRUD.leerFacultadPorId(id)
        if (facultadExistente == null) {
            println("No se encontró la facultad con ID: $id")
            return
        }

        val profesoresAsociados = ProfesorCRUD.leerProfesores().filter { it.facultadId == id }
        if (profesoresAsociados.isNotEmpty()) {
            println("ADVERTENCIA: Esta facultad tiene ${profesoresAsociados.size} profesores asociados.")
            print("¿Desea eliminar la facultad y sus profesores? (s/n): ")
            if (readLine()?.lowercase() != "s") {
                println("Operación cancelada.")
                return
            }
        }

        try {
            FacultadCRUD.eliminarFacultad(id)
        } catch (e: Exception) {
            println("Error al eliminar la facultad: ${e.message}")
        }
    }

    private fun menuProfesores() {
        while (true) {
            println("=== GESTIÓN DE PROFESORES ===")
            println("1. Crear Profesor")
            println("2. Leer Profesores")
            println("3. Actualizar Profesor")
            println("4. Eliminar Profesor")
            println("5. Volver al Menú Principal")
            print("Elige una opción: ")

            when (readLine()?.toIntOrNull()) {
                1 -> crearProfesor()
                2 -> leerProfesores()
                3 -> actualizarProfesor()
                4 -> eliminarProfesor()
                5 -> return
                else -> println("Opción no válida. Intenta de nuevo.")
            }
        }
    }

    private fun crearProfesor() {
        print("ID del profesor: ")
        val id = readLine()?.toIntOrNull() ?: return

        print("Nombre del profesor: ")
        val nombre = readLine() ?: return

        print("Apellido del profesor: ")
        val apellido = readLine() ?: return

        print("Fecha de nacimiento (yyyy-MM-dd): ")
        val fechaNacimiento = readLine()?.let { LocalDate.parse(it) } ?: return

        print("¿Está activo? (true/false): ")
        val activo = readLine()?.toBoolean() ?: return

        val facultades = FacultadCRUD.leerFacultades()
        if (facultades.isEmpty()) {
            println("No hay facultades disponibles. Primero cree una facultad.")
            return
        }

        println("Facultades disponibles:")
        facultades.forEach { facultad ->
            println("${facultad.id}. ${facultad.nombre}")
        }

        print("Seleccione el ID de la facultad: ")
        val facultadId = readLine()?.toIntOrNull() ?: return

        if (facultades.none { it.id == facultadId }) {
            println("ID de facultad no válido.")
            return
        }

        try {
            ProfesorCRUD.crearProfesor(id, nombre, apellido, fechaNacimiento, activo, facultadId)
        } catch (e: Exception) {
            println("Error al crear el profesor: ${e.message}")
        }
    }

    private fun leerProfesores() {
        val profesores = ProfesorCRUD.leerProfesores()
        if (profesores.isEmpty()) {
            println("No hay profesores registrados.")
            return
        }

        profesores.forEach { profesor: Profesor ->
            val facultad = FacultadCRUD.leerFacultadPorId(profesor.facultadId)
            println("\nProfesor ID: ${profesor.id}")
            println("Nombre: ${profesor.nombre} ${profesor.apellido}")
            println("Fecha de Nacimiento: ${profesor.fechaNacimiento}")
            println("Activo: ${profesor.activo}")
            println("Facultad: ${facultad?.nombre ?: "No encontrada"}")
        }
    }

    private fun actualizarProfesor() {
        println("Ingrese el ID del profesor a actualizar: ")
        val id = readLine()?.toIntOrNull() ?: return

        val profesorExistente = ProfesorCRUD.leerProfesorPorId(id)
        if (profesorExistente == null) {
            println("No se encontró el profesor con ID: $id")
            return
        }

        println("Actualizando el profesor ${profesorExistente.nombre} ${profesorExistente.apellido}:")

        print("Nuevo nombre (${profesorExistente.nombre}): ")
        val nuevoNombre = readLine()?.takeIf { it.isNotBlank() } ?: profesorExistente.nombre

        print("Nuevo apellido (${profesorExistente.apellido}): ")
        val nuevoApellido = readLine()?.takeIf { it.isNotBlank() } ?: profesorExistente.apellido

        print("Nueva fecha de nacimiento (${profesorExistente.fechaNacimiento}) [yyyy-MM-dd]: ")
        val nuevaFechaNacimiento = readLine()?.let { LocalDate.parse(it) } ?: profesorExistente.fechaNacimiento

        print("¿Está activo? (${profesorExistente.activo}) [true/false]: ")
        val nuevoActivo = readLine()?.toBoolean() ?: profesorExistente.activo

        try {
            ProfesorCRUD.actualizarProfesor(id, nuevoNombre, nuevoApellido, nuevaFechaNacimiento, nuevoActivo)
        } catch (e: Exception) {
            println("Error al actualizar el profesor: ${e.message}")
        }
    }

    private fun eliminarProfesor() {
        println("Ingrese el ID del profesor a eliminar: ")
        val id = readLine()?.toIntOrNull() ?: return

        val profesorExistente = ProfesorCRUD.leerProfesorPorId(id)
        if (profesorExistente == null) {
            println("No se encontró el profesor con ID: $id")
            return
        }

        try {
            ProfesorCRUD.eliminarProfesor(id)
        } catch (e: Exception) {
            println("Error al eliminar el profesor: ${e.message}")
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val app = MainActivity()
            app.iniciar()
        }
    }
}