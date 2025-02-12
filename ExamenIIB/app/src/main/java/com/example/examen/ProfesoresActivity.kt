package com.example.examen

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate

class ProfesoresActivity : AppCompatActivity() {
    private lateinit var adapter: ProfesorAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profesores)

        recyclerView = findViewById(R.id.rvProfesores)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProfesorAdapter(
            profesores = emptyList(),
            onEditClick = { profesor -> mostrarDialogoEditar(profesor) },
            onDeleteClick = { profesor -> confirmarEliminar(profesor) }
        )

        recyclerView.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAgregarProfesor).setOnClickListener {
            mostrarDialogoCrear()
        }

        cargarProfesores()
    }

    private fun cargarProfesores() {
        val profesores = ProfesorCRUD.leerProfesores()
        adapter.updateProfesores(profesores)
    }

    private fun setupFacultadesSpinner(spinner: Spinner): ArrayAdapter<Facultad> {
        val facultades = FacultadCRUD.leerFacultades()
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            facultades
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        return adapter
    }

    private fun mostrarDialogoCrear() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_profesor, null)
        val spinner = dialogView.findViewById<Spinner>(R.id.spinnerFacultad)
        val facultadesAdapter = setupFacultadesSpinner(spinner)

        AlertDialog.Builder(this)
            .setTitle("Nuevo Profesor")
            .setView(dialogView)
            .setPositiveButton("Guardar") { dialog, _ ->
                try {
                    val id = dialogView.findViewById<TextInputEditText>(R.id.etId).text.toString().toInt()
                    val nombre = dialogView.findViewById<TextInputEditText>(R.id.etNombre).text.toString()
                    val apellido = dialogView.findViewById<TextInputEditText>(R.id.etApellido).text.toString()
                    val fechaStr = dialogView.findViewById<TextInputEditText>(R.id.etFechaNacimiento).text.toString()
                    val fecha = LocalDate.parse(fechaStr)
                    val activo = dialogView.findViewById<SwitchMaterial>(R.id.switchActivo).isChecked
                    val facultad = spinner.selectedItem as Facultad

                    ProfesorCRUD.crearProfesor(id, nombre, apellido, fecha, activo, facultad.id)
                    cargarProfesores()
                    Toast.makeText(this, "Profesor creado exitosamente", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEditar(profesor: Profesor) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_profesor, null)
        val spinner = dialogView.findViewById<Spinner>(R.id.spinnerFacultad)
        val facultadesAdapter = setupFacultadesSpinner(spinner)

        // Pre-llenar los campos
        dialogView.findViewById<TextInputEditText>(R.id.etId).apply {
            setText(profesor.id.toString())
            isEnabled = false
        }
        dialogView.findViewById<TextInputEditText>(R.id.etNombre).setText(profesor.nombre)
        dialogView.findViewById<TextInputEditText>(R.id.etApellido).setText(profesor.apellido)
        dialogView.findViewById<TextInputEditText>(R.id.etFechaNacimiento).setText(profesor.fechaNacimiento.toString())
        dialogView.findViewById<SwitchMaterial>(R.id.switchActivo).isChecked = profesor.activo

        // Seleccionar la facultad actual
        val facultadPosition = facultadesAdapter.getPosition(
            facultadesAdapter.getItem(0)?.let { facultad ->
                facultadesAdapter.getItem(
                    facultadesAdapter.getCount() - 1
                )?.takeIf { it.id == profesor.facultadId }
            }
        )
        if (facultadPosition >= 0) {
            spinner.setSelection(facultadPosition)
        }

        AlertDialog.Builder(this)
            .setTitle("Editar Profesor")
            .setView(dialogView)
            .setPositiveButton("Actualizar") { dialog, _ ->
                try {
                    val nombre = dialogView.findViewById<TextInputEditText>(R.id.etNombre).text.toString()
                    val apellido = dialogView.findViewById<TextInputEditText>(R.id.etApellido).text.toString()
                    val fechaStr = dialogView.findViewById<TextInputEditText>(R.id.etFechaNacimiento).text.toString()
                    val fecha = LocalDate.parse(fechaStr)
                    val activo = dialogView.findViewById<SwitchMaterial>(R.id.switchActivo).isChecked

                    ProfesorCRUD.actualizarProfesor(profesor.id, nombre, apellido, fecha, activo)
                    cargarProfesores()
                    Toast.makeText(this, "Profesor actualizado exitosamente", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmarEliminar(profesor: Profesor) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Profesor")
            .setMessage("¿Está seguro que desea eliminar al profesor ${profesor.nombre} ${profesor.apellido}?")
            .setPositiveButton("Eliminar") { dialog, _ ->
                try {
                    ProfesorCRUD.eliminarProfesor(profesor.id)
                    cargarProfesores()
                    Toast.makeText(this, "Profesor eliminado exitosamente", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al eliminar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}