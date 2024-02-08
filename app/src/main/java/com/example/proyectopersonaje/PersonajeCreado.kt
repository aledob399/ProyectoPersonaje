package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PersonajeCreado : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var personaje: Personaje? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personaje_creado)

        dbHelper = DatabaseHelper(this)

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val btnEmpezar = findViewById<Button>(R.id.btnEmpezar)
        personaje = intent.getParcelableExtra("personaje")
        var datos: TextView = findViewById(R.id.datos)
        var img: ImageView = findViewById(R.id.img)

        // Guardar el personaje en la base de datos si es nuevo o si se reanuda la actividad
        guardarPersonajeEnBaseDeDatos()

        datos.text = personaje.toString()

        val idImagen = intent.getIntExtra("img", 0)
        img.setImageResource(idImagen)

        btnVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btnEmpezar.setOnClickListener {
            val intent = Intent(this, Aventura::class.java)
            intent.putExtra("personaje", personaje)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        guardarPersonajeEnBaseDeDatos()
    }

    private fun guardarPersonajeEnBaseDeDatos() {
        personaje?.let {
            if (!dbHelper.contienePersonaje()) {
                dbHelper.insertarPersonaje(it)
            } else {
                dbHelper.actualizarPersonaje(it)
            }
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
