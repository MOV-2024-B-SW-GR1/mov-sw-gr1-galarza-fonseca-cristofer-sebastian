package com.example.examen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate

class FacultadesActivity : AppCompatActivity() {
    private lateinit var adapter: FacultadAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facultades)

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.rvFacultades)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar el adapter
        adapter = FacultadAdapter(
            facultades = emptyList(),
            onEditClick = { facultad -> mostrarDialogoEditar(facultad) },
            onDeleteClick = { facultad -> confirmarEliminar(facultad) },
            onMapClick = { facultad -> abrirMapa(facultad) }
        )

        recyclerView.adapter = adapter

        // Configurar FAB
        findViewById<FloatingActionButton>(R.id.fabAgregarFacultad).setOnClickListener {
            mostrarDialogoCrear()
        }

        // Cargar datos iniciales
        cargarFacultades()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_facultades, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_ver_mapa -> {
                val facultad = adapter.facultades.firstOrNull()
                facultad?.let {
                    abrirMapa(it)
                } ?: run {
                    Toast.makeText(this, "No hay facultades registradas", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cargarFacultades() {
        try {
            val facultades = FacultadCRUD.leerFacultades()
            adapter.updateFacultades(facultades)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al cargar facultades: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun mostrarDialogoCrear() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_facultad, null)

        AlertDialog.Builder(this)
            .setTitle("Nueva Facultad")
            .setView(dialogView)
            .setPositiveButton("Guardar") { dialog, _ ->
                try {
                    val id = dialogView.findViewById<TextInputEditText>(R.id.etId).text.toString().toInt()
                    val nombre = dialogView.findViewById<TextInputEditText>(R.id.etNombre).text.toString()
                    val ubicacion = dialogView.findViewById<TextInputEditText>(R.id.etUbicacion).text.toString()
                    val fechaStr = dialogView.findViewById<TextInputEditText>(R.id.etFechaFundacion).text.toString()
                    val fecha = LocalDate.parse(fechaStr)
                    val presupuesto = dialogView.findViewById<TextInputEditText>(R.id.etPresupuesto).text.toString().toDouble()
                    val latitud = dialogView.findViewById<TextInputEditText>(R.id.etLatitud).text.toString().toDoubleOrNull()
                    val longitud = dialogView.findViewById<TextInputEditText>(R.id.etLongitud).text.toString().toDoubleOrNull()

                    if (nombre.isBlank() || ubicacion.isBlank()) {
                        throw IllegalArgumentException("Todos los campos son obligatorios")
                    }

                    FacultadCRUD.crearFacultad(id, nombre, ubicacion, fecha, presupuesto, latitud, longitud)
                    cargarFacultades()
                    Toast.makeText(this, "Facultad creada exitosamente", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEditar(facultad: Facultad) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_facultad, null)

        // Pre-llenar los campos
        dialogView.findViewById<TextInputEditText>(R.id.etId).apply {
            setText(facultad.id.toString())
            isEnabled = false  // No permitir editar el ID
        }
        dialogView.findViewById<TextInputEditText>(R.id.etNombre).setText(facultad.nombre)
        dialogView.findViewById<TextInputEditText>(R.id.etUbicacion).setText(facultad.ubicacion)
        dialogView.findViewById<TextInputEditText>(R.id.etFechaFundacion).setText(facultad.fechaFundacion.toString())
        dialogView.findViewById<TextInputEditText>(R.id.etPresupuesto).setText(facultad.presupuesto.toString())
        dialogView.findViewById<TextInputEditText>(R.id.etLatitud).setText(facultad.latitud?.toString())
        dialogView.findViewById<TextInputEditText>(R.id.etLongitud).setText(facultad.longitud?.toString())

        AlertDialog.Builder(this)
            .setTitle("Editar Facultad")
            .setView(dialogView)
            .setPositiveButton("Actualizar") { dialog, _ ->
                try {
                    val nombre = dialogView.findViewById<TextInputEditText>(R.id.etNombre).text.toString()
                    val ubicacion = dialogView.findViewById<TextInputEditText>(R.id.etUbicacion).text.toString()
                    val fechaStr = dialogView.findViewById<TextInputEditText>(R.id.etFechaFundacion).text.toString()
                    val fecha = LocalDate.parse(fechaStr)
                    val presupuesto = dialogView.findViewById<TextInputEditText>(R.id.etPresupuesto).text.toString().toDouble()
                    val latitud = dialogView.findViewById<TextInputEditText>(R.id.etLatitud).text.toString().toDoubleOrNull()
                    val longitud = dialogView.findViewById<TextInputEditText>(R.id.etLongitud).text.toString().toDoubleOrNull()

                    if (nombre.isBlank() || ubicacion.isBlank()) {
                        throw IllegalArgumentException("Todos los campos son obligatorios")
                    }

                    FacultadCRUD.actualizarFacultad(facultad.id, nombre, ubicacion, fecha, presupuesto, latitud, longitud)
                    cargarFacultades()
                    Toast.makeText(this, "Facultad actualizada exitosamente", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmarEliminar(facultad: Facultad) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Facultad")
            .setMessage("¿Está seguro que desea eliminar la facultad ${facultad.nombre}?\nEsta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { dialog, _ ->
                try {
                    FacultadCRUD.eliminarFacultad(facultad.id)
                    cargarFacultades()
                    Toast.makeText(this, "Facultad eliminada exitosamente", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al eliminar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun abrirMapa(facultad: Facultad) {
        val intent = Intent(this, MapaFacultadActivity::class.java).apply {
            putExtra("latitud", facultad.latitud)
            putExtra("longitud", facultad.longitud)
        }
        startActivity(intent)
    }
}