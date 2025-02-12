package com.example.examen

import java.time.LocalDate

data class Profesor(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: LocalDate,
    val activo: Boolean,
    val facultadId: Int // Campo para almacenar la referencia de la facultad
)