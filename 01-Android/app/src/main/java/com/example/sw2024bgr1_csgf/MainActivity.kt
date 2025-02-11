package com.example.sw2024bgr1_csgf

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar el contexto de DatabaseManager
        DatabaseManager.init(this)

        try {
            // Inicializar la base de datos
            DatabaseManager.getInstance()

            // Configurar botones
            findViewById<Button>(R.id.btnFacultades).setOnClickListener {
                val intent = Intent(this, FacultadesActivity::class.java)
                startActivity(intent)
            }

            findViewById<Button>(R.id.btnProfesores).setOnClickListener {
                val intent = Intent(this, ProfesoresActivity::class.java)
                startActivity(intent)
            }

            findViewById<Button>(R.id.btnVerificarDB).setOnClickListener {
                val intent = Intent(this, VerificarDBActivity::class.java)
                startActivity(intent)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al inicializar: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}