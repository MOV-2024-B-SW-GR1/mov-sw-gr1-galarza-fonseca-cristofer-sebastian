package com.example.examen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class FacultadAdapter(
    private var facultades: List<Facultad>,
    private val onEditClick: (Facultad) -> Unit,
    private val onDeleteClick: (Facultad) -> Unit
) : RecyclerView.Adapter<FacultadAdapter.FacultadViewHolder>() {

    class FacultadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreFacultad: TextView = view.findViewById(R.id.tvNombreFacultad)
        val ubicacion: TextView = view.findViewById(R.id.tvUbicacion)
        val fechaFundacion: TextView = view.findViewById(R.id.tvFechaFundacion)
        val presupuesto: TextView = view.findViewById(R.id.tvPresupuesto)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacultadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_facultad, parent, false)
        return FacultadViewHolder(view)
    }

    override fun onBindViewHolder(holder: FacultadViewHolder, position: Int) {
        val facultad = facultades[position]
        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "EC"))

        holder.nombreFacultad.text = facultad.nombre
        holder.ubicacion.text = "Ubicación: ${facultad.ubicacion}"
        holder.fechaFundacion.text = "Fundación: ${facultad.fechaFundacion}"
        holder.presupuesto.text = "Presupuesto: ${formatoMoneda.format(facultad.presupuesto)}"

        holder.btnEditar.setOnClickListener { onEditClick(facultad) }
        holder.btnEliminar.setOnClickListener { onDeleteClick(facultad) }
        holder.itemView.setOnClickListener { onMapClick(facultad) }
    }

    override fun getItemCount() = facultades.size

    fun updateFacultades(newFacultades: List<Facultad>) {
        facultades = newFacultades
        notifyDataSetChanged()
    }
}