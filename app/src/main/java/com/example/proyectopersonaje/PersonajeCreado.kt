package com.example.proyectopersonaje

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
    private var mascotas: ArrayList<Mascota>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personaje_creado)

        dbHelper = DatabaseHelper(this)

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val btnEmpezar = findViewById<Button>(R.id.btnEmpezar)


        val modoRegistro = intent.getBooleanExtra("modoRegistro", false)
        if (modoRegistro) {
            personaje = guardarPersonajeEnBaseDeDatos(intent.getParcelableExtra("personaje"))


        } else {
            personaje = cargarPersonajeDesdeBaseDeDatos(intent.getParcelableExtra("personaje"))
            mascotas= cargarMascotasDesdeBaseDeDatos()
        }







        var datos: TextView = findViewById(R.id.datos)
        var img: ImageView = findViewById(R.id.img)

        // Guardar el personaje en la base de datos si es nuevo o si se reanuda la actividad
        if(mascotas==null){
            datos.text =personaje.toString()
        }else{
            datos.text =personaje.toString()+mascotas.toString()
        }




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
            intent.putParcelableArrayListExtra("mascotas", mascotas)

            startActivity(intent)
        }
    }



    private fun guardarPersonajeEnBaseDeDatos(personaje: Personaje?): Personaje? {
        val idUsuarioAuth = FirebaseAuth.getInstance().uid
        var personajeGuardado: Personaje? = null

        if (personaje != null) {
            dbHelper.insertarPersonaje(personaje)
            Toast.makeText(this, "Personaje guardado correctamente", Toast.LENGTH_SHORT).show()
            personajeGuardado = personaje
        } else {
            idUsuarioAuth?.let {
                personajeGuardado = dbHelper.obtenerPersonaje(it)
            }
        }

        return personajeGuardado
    }
    private fun cargarPersonajeDesdeBaseDeDatos(personaje: Personaje?): Personaje? {
        val idUsuarioAuth = FirebaseAuth.getInstance().uid

        return if (personaje != null) {

            dbHelper.obtenerPersonaje(idUsuarioAuth!!)
        } else {
            null
        }
    }
    private fun guardarMascotasEnBaseDeDatos(mascotas: ArrayList<Mascota>) {
        dbHelper.insertarMascotas(mascotas)
        Toast.makeText(this, "Mascotas guardadas correctamente", Toast.LENGTH_SHORT).show()
    }

    private fun cargarMascotasDesdeBaseDeDatos(): ArrayList<Mascota> {
        var mascotas = ArrayList<Mascota>()

         mascotas=dbHelper.obtenerMascotas()
        return mascotas
    }



}
