package com.example.sw2024bgr1_csgf

import java.time.LocalDate

fun main() {
    // Menú principal
    while (true) {
        println("=== Menú Principal ===")
        println("1. Gestión de Facultades")
        println("2. Gestión de Profesores")
        println("3. Salir")
        print("Elige una opción: ")

        when (readLine()?.toIntOrNull()) {
            1 -> menuFacultades()  // Menú para gestionar facultades
            2 -> menuProfesores()  // Menú para gestionar profesores
            3 -> {
                println("Saliendo del programa...")
                break
            }
            else -> println("Opción no válida. Intenta de nuevo.")
        }
    }
}

// Menú de Facultades
fun menuFacultades() {
    while (true) {
        println("=== GESTIÓN DE FACULTADES ===")
        println("1. Crear Facultad")
        println("2. Leer Facultades")
        println("3. Actualizar Facultad")
        println("4. Eliminar Facultad")
        println("5. Volver al Menú Principal")
        print("Elige una opción: ")

        when (readLine()?.toIntOrNull()) {
            1 -> crearFacultad()  // Crear facultad
            2 -> mostrarFacultadesConProfesores()  // Mostrar facultades con sus profesores asociados
            3 -> actualizarFacultad()  // Actualizar facultad
            4 -> eliminarFacultad()  // Eliminar facultad
            5 -> return  // Volver al menú principal
            else -> println("Opción no válida. Intenta de nuevo.")
        }
    }
}

// Crear una facultad
fun crearFacultad() {
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

    FacultadCRUD.crearFacultad(id, nombre, ubicacion, fecha, presupuesto)
}

// Mostrar facultades con profesores asociados
fun mostrarFacultadesConProfesores() {
    val facultades = FacultadCRUD.leerFacultades()
    val profesores = ProfesorCRUD.leerProfesores()  // Asumiendo que tienes un método para leer los profesores desde su archivo

    if (facultades.isEmpty()) {
        println("No hay facultades registradas.")
    } else {
        facultades.forEach { facultad ->
            println("Facultad ID: ${facultad.id}")
            println("Nombre: ${facultad.nombre}")
            println("Ubicación: ${facultad.ubicacion}")
            println("Fecha de Fundación: ${facultad.fechaFundacion}")
            println("Presupuesto: ${facultad.presupuesto}")
            println("Profesores:")

            // Filtrar profesores por el ID de la facultad
            val profesoresDeLaFacultad = profesores.filter { it.facultadId == facultad.id }

            if (profesoresDeLaFacultad.isEmpty()) {
                println("  No hay profesores asociados.")
            } else {
                profesoresDeLaFacultad.forEach { profesor ->
                    println("  - Profesor ID: ${profesor.id}, Nombre: ${profesor.nombre} ${profesor.apellido}")
                }
            }
            println()
        }
    }
}

// Actualizar una facultad
fun actualizarFacultad() {
    println("Ingrese el ID de la facultad a actualizar: ")
    val id = readLine()?.toIntOrNull() ?: return

    val facultadExistente = FacultadCRUD.leerFacultades().find { it.id == id }

    if (facultadExistente == null) {
        println("No se encontró la facultad con ID: $id")
        return
    }

    println("Actualizando la facultad ${facultadExistente.nombre}:")
    print("Nuevo nombre de la facultad: ")
    val nombre = readLine() ?: facultadExistente.nombre

    print("Nueva ubicación de la facultad: ")
    val ubicacion = readLine() ?: facultadExistente.ubicacion

    print("Nueva fecha de fundación (yyyy-MM-dd): ")
    val fecha = readLine()?.let { LocalDate.parse(it) } ?: facultadExistente.fechaFundacion

    print("Nuevo presupuesto de la facultad: ")
    val presupuesto = readLine()?.toDoubleOrNull() ?: facultadExistente.presupuesto

    FacultadCRUD.actualizarFacultad(id, nombre, ubicacion, fecha, presupuesto)
}

// Eliminar una facultad
fun eliminarFacultad() {
    println("Ingrese el ID de la facultad a eliminar: ")
    val id = readLine()?.toIntOrNull() ?: return

    val facultadExistente = FacultadCRUD.leerFacultades().find { it.id == id }

    if (facultadExistente == null) {
        println("No se encontró la facultad con ID: $id")
        return
    }

    FacultadCRUD.eliminarFacultad(id)
    println("Facultad eliminada exitosamente.")
}

// Menú de Profesores
fun menuProfesores() {
    while (true) {
        println("=== GESTIÓN DE PROFESORES ===")
        println("1. Crear Profesor")
        println("2. Leer Profesores")
        println("3. Actualizar Profesor")
        println("4. Eliminar Profesor")
        println("5. Volver al Menú Principal")
        print("Elige una opción: ")

        when (readLine()?.toIntOrNull()) {
            1 -> crearProfesor()  // Crear un profesor
            2 -> leerProfesores()  // Leer y mostrar los profesores
            3 -> actualizarProfesor()  // Actualizar un profesor
            4 -> eliminarProfesor()  // Eliminar un profesor
            5 -> return  // Volver al menú principal
            else -> println("Opción no válida. Intenta de nuevo.")
        }
    }
}



// Crear un profesor
fun crearProfesor() {
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

    println("Seleccione la facultad a la que pertenece el profesor:")
    val facultades = FacultadCRUD.leerFacultades()
    if (facultades.isEmpty()) {
        println("No hay facultades disponibles. Primero crea una facultad.")
        return
    }

    facultades.forEachIndexed { index, facultad ->
        println("${index + 1}. ${facultad.nombre}")
    }

    val facultadIndex = readLine()?.toIntOrNull()?.minus(1) ?: return
    if (facultadIndex !in facultades.indices) {
        println("Facultad no válida.")
        return
    }

    val facultadSeleccionada = facultades[facultadIndex]

    // Crear el profesor y asociarlo a la facultad seleccionada
    ProfesorCRUD.crearProfesor(id, nombre, apellido, fechaNacimiento, activo, facultadSeleccionada.id)
}

// Leer y mostrar los profesores
fun leerProfesores() {
    val profesores = ProfesorCRUD.leerProfesores()
    if (profesores.isEmpty()) {
        println("No hay profesores registrados.")
    } else {
        profesores.forEach { profesor ->
            println("Profesor ID: ${profesor.id}")
            println("Nombre: ${profesor.nombre} ${profesor.apellido}")
            println("Fecha de Nacimiento: ${profesor.fechaNacimiento}")
            println("Activo: ${profesor.activo}")
            println("Facultad ID: ${profesor.facultadId}")
            println()
        }
    }
}

// Actualizar un profesor
fun actualizarProfesor() {
    println("Ingrese el ID del profesor a actualizar: ")
    val id = readLine()?.toIntOrNull() ?: return

    val profesorExistente = ProfesorCRUD.leerProfesores().find { it.id == id }

    if (profesorExistente == null) {
        println("No se encontró el profesor con ID: $id")
        return
    }

    println("Actualizando el profesor ${profesorExistente.nombre}:")
    print("Nuevo nombre del profesor (actual: ${profesorExistente.nombre}): ")
    val nuevoNombre = readLine() ?: profesorExistente.nombre

    print("Nuevo apellido del profesor (actual: ${profesorExistente.apellido}): ")
    val nuevoApellido = readLine() ?: profesorExistente.apellido

    print("Nueva fecha de nacimiento (yyyy-MM-dd) (actual: ${profesorExistente.fechaNacimiento}): ")
    val nuevaFechaNacimiento = readLine()?.let { LocalDate.parse(it) } ?: profesorExistente.fechaNacimiento

    print("¿Está activo? (true/false) (actual: ${profesorExistente.activo}): ")
    val nuevoActivo = readLine()?.toBoolean() ?: profesorExistente.activo

    // Actualizar el profesor
    ProfesorCRUD.actualizarProfesor(id, nuevoNombre, nuevoApellido, nuevaFechaNacimiento, nuevoActivo)
}

// Eliminar un profesor
fun eliminarProfesor() {
    println("Ingrese el ID del profesor a eliminar: ")
    val id = readLine()?.toIntOrNull() ?: return

    val profesorExistente = ProfesorCRUD.leerProfesores().find { it.id == id }

    if (profesorExistente == null) {
        println("No se encontró el profesor con ID: $id")
        return
    }

    ProfesorCRUD.eliminarProfesor(id)
    println("Profesor eliminado exitosamente.")
}