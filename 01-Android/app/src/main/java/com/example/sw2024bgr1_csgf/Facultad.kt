package com.example.sw2024bgr1_csgf

import java.time.LocalDate

data class Facultad(
    val id: Int,
    val nombre: String,
    val ubicacion: String,
    val fechaFundacion: LocalDate,
    val presupuesto: Double,
    val profesores: MutableList<Profesor> = mutableListOf() // Lista de profesores asociados
)