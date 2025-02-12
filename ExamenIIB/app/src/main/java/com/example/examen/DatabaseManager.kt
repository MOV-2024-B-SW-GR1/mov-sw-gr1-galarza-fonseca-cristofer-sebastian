package com.example.examen

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "universidad.db"
        private const val DATABASE_VERSION = 1

        @Volatile
        private var INSTANCE: DatabaseManager? = null
        private lateinit var APPLICATION_CONTEXT: Context

        // Método para inicializar el contexto de la aplicación
        fun init(context: Context) {
            APPLICATION_CONTEXT = context.applicationContext
        }

        fun getInstance(): DatabaseManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DatabaseManager(APPLICATION_CONTEXT).also { INSTANCE = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla Facultades
        db.execSQL("""
             CREATE TABLE IF NOT EXISTS facultades (
            id INTEGER PRIMARY KEY,
            nombre TEXT NOT NULL,
            ubicacion TEXT NOT NULL,
            fecha_fundacion TEXT NOT NULL,
            presupuesto REAL NOT NULL,
            latitud REAL,
            longitud REAL
        )
        """)

        // Crear tabla Profesores
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS profesores (
                id INTEGER PRIMARY KEY,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                fecha_nacimiento TEXT NOT NULL,
                activo INTEGER NOT NULL,
                facultad_id INTEGER,
                FOREIGN KEY (facultad_id) REFERENCES facultades(id)
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Eliminar tablas antiguas y recrearlas
        db.execSQL("DROP TABLE IF EXISTS profesores")
        db.execSQL("DROP TABLE IF EXISTS facultades")
        onCreate(db)
    }

    fun getConnection(): SQLiteDatabase = writableDatabase
}