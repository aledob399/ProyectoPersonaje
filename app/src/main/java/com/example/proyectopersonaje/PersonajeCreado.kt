package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class PersonajeCreado : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var personaje: Personaje? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personaje_creado)

        dbHelper = DatabaseHelper(this)

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val btnEmpezar = findViewById<Button>(R.id.btnEmpezar)


        val modoRegistro = intent.getBooleanExtra("modoRegistro", false)

        // Guardar el personaje en la base de datos si es nuevo o si se reanuda la actividad
        personaje = guardarPersonajeEnBaseDeDatos(intent.getParcelableExtra("personaje"), modoRegistro)




        var datos: TextView = findViewById(R.id.datos)
        var img: ImageView = findViewById(R.id.img)

        // Guardar el personaje en la base de datos si es nuevo o si se reanuda la actividad


        datos.text =personaje.toString()

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



    private fun guardarPersonajeEnBaseDeDatos(personaje: Personaje?, modoRegistro: Boolean): Personaje? {
        val idUsuarioAuth = FirebaseAuth.getInstance().uid
        var personajeGuardado: Personaje? = null

        if (personaje != null && modoRegistro) {
            dbHelper.insertarPersonaje(personaje)
            Toast.makeText(this, "Objeto comprado correctamente", Toast.LENGTH_SHORT).show()
            personajeGuardado = personaje
        } else {
            idUsuarioAuth?.let {
                personajeGuardado = dbHelper.obtenerPersonaje(it)
            }
        }

        return personajeGuardado
    }


}
