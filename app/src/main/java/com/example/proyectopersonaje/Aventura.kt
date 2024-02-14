package com.example.proyectopersonaje


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Looper

import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.logging.Handler


class Aventura : AppCompatActivity() {
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var openDrawer: Button
    private lateinit var dbHelper: DatabaseHelper
    @SuppressLint("ResourceType", "WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aventura)
        val btn=findViewById<ImageButton>(R.id.btn)
        val personaje=intent.getParcelableExtra<Personaje>("personaje")
       // val mascotas = intent.getParcelableArrayListExtra<Mascota>("mascotas")
        val db=DatabaseHelper(this)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigationview)
        openDrawer = findViewById(R.id.btn_open)
        dbHelper = DatabaseHelper(this)
        val dado=Dado()
        var num2=0
        var num1=0
        val strings = mutableListOf<String>()
        strings.add("R.drawable.uno")
        strings.add("R.drawable.dos")
        strings.add("R.drawable.tres")
        strings.add("R.drawable.cuatro")
        strings.add("R.drawable.cinco")
        strings.add("R.drawable.seis")
        openDrawer.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            }
            drawerLayout.openDrawer(GravityCompat.END)

            navigationView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.one -> {
                        drawerLayout.closeDrawer(GravityCompat.END)
                        // Llamada para actualizar el personaje
                        if (personaje != null) {
                          //  guardarMascotasEnBaseDeDatos(mascotas!!)
                            guardarPersonajeEnBaseDeDatos(personaje)

                            Toast.makeText(this, "Personaje actualizado con éxito", Toast.LENGTH_SHORT).show()
                        }
                        return@setNavigationItemSelectedListener true
                    }

                    else -> {
                        return@setNavigationItemSelectedListener false
                    }
                }
            }
        }

        btn.setOnClickListener {
            num1=0
            num2=0
            when (dado.tiradaUnica()) {

                1 -> {
                    repeat(20) { index ->
                        android.os.Handler(Looper.getMainLooper()).postDelayed({
                            val randomIndex = (2..6).random() // Generar un índice aleatorio para seleccionar una imagen diferente
                            val imageResource = when (randomIndex) {
                                2 -> R.drawable.dos
                                3 -> R.drawable.tres
                                4 -> R.drawable.cuatro
                                5 -> R.drawable.cinco
                                6 -> R.drawable.seis
                                else -> R.drawable.uno
                            }
                            btn.setImageResource(imageResource)
                        }, (index + 1) * 5000L)
                    }

                    btn.setImageResource(R.drawable.uno)
                    val intent = Intent(this,Objeto::class.java)
                    intent.putExtra("personaje",personaje)
                   // intent.putParcelableArrayListExtra("mascotas", mascotas)

                    startActivity(intent)
                }
                2 -> {

                    btn.setImageResource(R.drawable.dos)
                    val intent = Intent(this,Ciudad::class.java)
                    intent.putExtra("personaje",personaje)
                   // intent.putParcelableArrayListExtra("mascotas", mascotas)

                    startActivity(intent)
                }
                3 -> {

                    btn.setImageResource(R.drawable.tres)
                    val intent = Intent(this,Mazmorras::class.java)
                    intent.putExtra("personaje",personaje)
                   // intent.putParcelableArrayListExtra("mascotas", mascotas)

                    startActivity(intent)
                }
                4 -> {

                    btn.setImageResource(R.drawable.cuatro)
                    val intent = Intent(this,Mercader::class.java)
                    intent.putExtra("personaje",personaje)
                   // intent.putParcelableArrayListExtra("mascotas", mascotas)

                    startActivity(intent)
                }
                5 -> {

                    btn.setImageResource(R.drawable.cinco)
                    val intent = Intent(this,Mascotas::class.java)
                    intent.putExtra("personaje",personaje)
                   // intent.putParcelableArrayListExtra("mascotas", mascotas)

                    startActivity(intent)
                }
                6 -> {

                    btn.setImageResource(R.drawable.seis)
                    val intent = Intent(this,Objeto::class.java)
                    intent.putExtra("personaje",personaje)
                   // intent.putParcelableArrayListExtra("mascotas", mascotas)
                    startActivity(intent)
                }
            }
        }
    }
    private fun guardarPersonajeEnBaseDeDatos(personaje: Personaje?) {
        val idUsuarioAuth = FirebaseAuth.getInstance().uid
        var personajeGuardado: Personaje? = null

        if (personaje != null) {
            dbHelper.insertarPersonaje(personaje, idUsuarioAuth!!)
            Toast.makeText(this, "Personaje guardado correctamente", Toast.LENGTH_SHORT).show()
            personajeGuardado = personaje
        } else {
            Toast.makeText(this, "Personaje guardado correctamente", Toast.LENGTH_SHORT).show()
        }



    }
    private fun guardarMascotasEnBaseDeDatos(mascotas: ArrayList<Mascota>): ArrayList<Mascota>? {
        val idUsuarioAuth = FirebaseAuth.getInstance().uid

        if (mascotas.isNotEmpty()) {
            dbHelper.insertarMascotas(mascotas)
            Toast.makeText(this, "Mascotas guardadas correctamente", Toast.LENGTH_SHORT).show()
        } else {
            idUsuarioAuth?.let {
                val mascotasGuardadas = dbHelper.obtenerMascotas()
                // Asignar las mascotas de la base de datos a la lista de mascotas
                mascotas.clear()
                mascotas.addAll(mascotasGuardadas)
                return mascotas
            }
        }

        return mascotas
    }



}