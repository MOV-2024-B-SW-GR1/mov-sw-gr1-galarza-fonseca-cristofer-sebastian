package com.example.sw2024bgr1_csgf

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.text.NumberFormat
import java.util.Locale

class VerificarDBActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verificar_db)

        mostrarInformacionDB()
    }

    private fun mostrarInformacionDB() {
        val tvFacultadesInfo = findViewById<TextView>(R.id.tvFacultadesInfo)
        val tvProfesoresInfo = findViewById<TextView>(R.id.tvProfesoresInfo)
        val tvDBLocation = findViewById<TextView>(R.id.tvDBLocation)

        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "EC"))

        // Información de Facultades
        val facultades = FacultadCRUD.leerFacultades()
        val infoFacultades = StringBuilder()
        if (facultades.isEmpty()) {
            infoFacultades.append("No hay facultades registradas.\n")
        } else {
            infoFacultades.append("Total de facultades: ${facultades.size}\n\n")
            facultades.forEach { facultad ->
                infoFacultades.append("ID: ${facultad.id}\n")
                infoFacultades.append("Nombre: ${facultad.nombre}\n")
                infoFacultades.append("Ubicación: ${facultad.ubicacion}\n")
                infoFacultades.append("Fecha Fundación: ${facultad.fechaFundacion}\n")
                infoFacultades.append("Presupuesto: ${formatoMoneda.format(facultad.presupuesto)}\n")
                infoFacultades.append("------------------------\n")
            }
        }
        tvFacultadesInfo.text = infoFacultades.toString()

        // Información de Profesores
        val profesores = ProfesorCRUD.leerProfesores()
        val infoProfesores = StringBuilder()
        if (profesores.isEmpty()) {
            infoProfesores.append("No hay profesores registrados.\n")
        } else {
            infoProfesores.append("Total de profesores: ${profesores.size}\n\n")
            profesores.forEach { profesor ->
                val facultad = FacultadCRUD.leerFacultadPorId(profesor.facultadId)
                infoProfesores.append("ID: ${profesor.id}\n")
                infoProfesores.append("Nombre: ${profesor.nombre} ${profesor.apellido}\n")
                infoProfesores.append("Fecha Nacimiento: ${profesor.fechaNacimiento}\n")
                infoProfesores.append("Estado: ${if(profesor.activo) "Activo" else "Inactivo"}\n")
                infoProfesores.append("Facultad: ${facultad?.nombre ?: "No asignada"}\n")
                infoProfesores.append("------------------------\n")
            }
        }
        tvProfesoresInfo.text = infoProfesores.toString()

        // Ubicación de la base de datos
        val dbPath = getDatabasePath("universidad.db")
        tvDBLocation.text = "Ubicación de la base de datos:\n${dbPath.absolutePath}"
    }

    override fun getDatabasePath(dbName: String): File {
        return File(applicationContext.getDatabasePath(dbName).path)
    }
}