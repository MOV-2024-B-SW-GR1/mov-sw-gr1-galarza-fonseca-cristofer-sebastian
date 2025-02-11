package com.example.sw2024bgr1_csgf

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProfesorAdapter(
    private var profesores: List<Profesor>,
    private val onEditClick: (Profesor) -> Unit,
    private val onDeleteClick: (Profesor) -> Unit
) : RecyclerView.Adapter<ProfesorAdapter.ProfesorViewHolder>() {

    class ProfesorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreCompleto: TextView = view.findViewById(R.id.tvNombreCompleto)
        val fechaNacimiento: TextView = view.findViewById(R.id.tvFechaNacimiento)
        val facultad: TextView = view.findViewById(R.id.tvFacultad)
        val estado: TextView = view.findViewById(R.id.tvEstado)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfesorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profesor, parent, false)
        return ProfesorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfesorViewHolder, position: Int) {
        val profesor = profesores[position]
        val facultad = FacultadCRUD.leerFacultadPorId(profesor.facultadId)

        holder.nombreCompleto.text = "${profesor.nombre} ${profesor.apellido}"
        holder.fechaNacimiento.text = "Fecha de Nacimiento: ${profesor.fechaNacimiento}"
        holder.facultad.text = "Facultad: ${facultad?.nombre ?: "No asignada"}"
        holder.estado.text = "Estado: ${if(profesor.activo) "Activo" else "Inactivo"}"

        holder.btnEditar.setOnClickListener { onEditClick(profesor) }
        holder.btnEliminar.setOnClickListener { onDeleteClick(profesor) }
    }

    override fun getItemCount() = profesores.size

    fun updateProfesores(newProfesores: List<Profesor>) {
        profesores = newProfesores
        notifyDataSetChanged()
    }
}