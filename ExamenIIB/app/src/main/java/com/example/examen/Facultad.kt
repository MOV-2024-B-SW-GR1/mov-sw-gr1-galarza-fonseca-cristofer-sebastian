package com.example.examen

import java.time.LocalDate

data class Facultad(
    val id: Int,
    val nombre: String,
    val ubicacion: String,
    val fechaFundacion: LocalDate,
    val presupuesto: Double,
    val latitud: Double?,
    val longitud: Double?
)
