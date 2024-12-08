package com.example.sw2024bgr1_csgf

import java.io.File

object FileManager {
    fun readFromFile(fileName: String): List<String> {
        return try {
            File(fileName).readLines().map { it.trim() }
        } catch (e: Exception) {
            println("Error al leer el archivo: ${e.message}")
            emptyList()
        }
    }

    fun saveToFile(fileName: String, lines: List<String>) {
        try {
            File(fileName).writeText(lines.joinToString("\n"))
        } catch (e: Exception) {
            println("Error al guardar en el archivo: ${e.message}")
        }
    }
}