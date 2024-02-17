package com.example.proyectopersonaje


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Looper
import android.speech.tts.TextToSpeech

import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale
import java.util.logging.Handler


class Aventura : AppCompatActivity() {
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var openDrawer: Button
    private lateinit var dbHelper: Database
    private lateinit var textToSpeech:TextToSpeech
    @SuppressLint("ResourceType", "WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aventura)
        val btn=findViewById<ImageButton>(R.id.btn)
        val musica: MediaPlayer = MediaPlayer.create(this, R.raw.musica)
        val btnMusica: ImageButton = findViewById(R.id.btnMusica)
        val dbHelper=Database(this)
        val idUsuarioAuth = FirebaseAuth.getInstance().currentUser!!.uid.toString()
        val personaje=intent.getParcelableExtra<Personaje>("personaje")
        val creado=intent.getBooleanExtra("creado",false)

        var mascotas = intent.getParcelableArrayListExtra<Mascota>("mascotas")!!
        if(creado){
            personaje!!.getMochila().setContenido(dbHelper.obtenerArticulos(idUsuarioAuth).getContenido())
            personaje.getLibro().setContenido(dbHelper.obtenerMagias(idUsuarioAuth))
            mascotas=dbHelper.obtenerMascotas(idUsuarioAuth)
        }
        Log.d("DatosPersonaje", "$personaje")
        Log.d("DatosMascota 1", "${mascotas[0]}")
        val db=Database(this)


        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigationview)
        openDrawer = findViewById(R.id.btn_open)
        val objeto1 =
            Articulo(Articulo.TipoArticulo.ARMA, Articulo.Nombre.MARTILLO, 2, 34, R.drawable.objeto,1,Articulo.Rareza.COMUN)
        val mascota=Mascota("Marco" , Mascota.tipoMascota.AGUA)
        //personaje!!.getMochila().addArticulo(objeto1)


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
        var pos=0
        musica.isLooping = true
        musica.start()
        btnMusica.setOnClickListener {
            if(musica.isPlaying){
                pos = musica.currentPosition
                musica.pause()
                musica.isLooping = false
                btnMusica.setImageResource(android.R.drawable.ic_media_pause)
            }else{
                btnMusica.setImageResource(android.R.drawable.ic_media_previous)
                musica.seekTo(pos)
                musica.start()
                musica.isLooping = true
            }
        }
        textToSpeech = TextToSpeech(this){status->
            if (status == TextToSpeech.SUCCESS){
                val result = textToSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA||result == TextToSpeech.LANG_NOT_SUPPORTED){
                    Toast.makeText(this,"lenguaje no soportado",Toast.LENGTH_LONG).show()
                }
            }
        }
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
                            //personaje.setNivel(3)
                            dbHelper.borrarArticulos(idUsuarioAuth)
                            dbHelper.borrarMagias(idUsuarioAuth)
                            dbHelper.insertarPersonaje(personaje,idUsuarioAuth)
                            dbHelper.insertarArticulos(personaje.getMochila().getContenido(),idUsuarioAuth)
                            dbHelper.insertarMagias(personaje.getLibro().getContenido(),idUsuarioAuth)

                            Toast.makeText(this, "Personaje actualizado con éxito", Toast.LENGTH_SHORT).show()
                        }

                        dbHelper.borrarMascotas(idUsuarioAuth)
                        dbHelper.insertarMascotas(mascotas,idUsuarioAuth)
                        Toast.makeText(this, "Mascotas actualizadas con éxito", Toast.LENGTH_SHORT).show()


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
                    val intent = Intent(this,Contrato::class.java)

                    intent.putExtra("personaje",personaje)
                    intent.putExtra("mochila",personaje!!.getMochila())
                    intent.putParcelableArrayListExtra("mascotas", mascotas)

                    startActivity(intent)
                }
                2 -> {

                    btn.setImageResource(R.drawable.dos)
                    val intent = Intent(this,Contrato::class.java)
                    intent.putExtra("personaje",personaje)
                    intent.putExtra("mochila",personaje!!.getMochila())
                    intent.putParcelableArrayListExtra("mascotas", mascotas)

                    startActivity(intent)
                }
                3 -> {

                    btn.setImageResource(R.drawable.tres)
                    val intent = Intent(this,Contrato::class.java)
                    intent.putExtra("personaje",personaje)
                    intent.putExtra("mochila",personaje!!.getMochila())
                    intent.putParcelableArrayListExtra("mascotas", mascotas)

                    startActivity(intent)
                }
                4 -> {

                    btn.setImageResource(R.drawable.cuatro)
                    val intent = Intent(this,Contrato::class.java)
                    intent.putExtra("personaje",personaje)
                    intent.putExtra("mochila",personaje!!.getMochila())
                    intent.putParcelableArrayListExtra("mascotas", mascotas)

                    startActivity(intent)
                }
                5 -> {

                    btn.setImageResource(R.drawable.cinco)
                    val intent = Intent(this,Contrato::class.java)
                    intent.putExtra("mochila",personaje!!.getMochila())
                    intent.putExtra("personaje",personaje)
                    intent.putParcelableArrayListExtra("mascotas", mascotas)

                    startActivity(intent)
                }
                6 -> {

                    btn.setImageResource(R.drawable.seis)
                    val intent = Intent(this,Contrato::class.java)
                    intent.putExtra("personaje",personaje)
                    intent.putExtra("mochila",personaje!!.getMochila())
                    intent.putParcelableArrayListExtra("mascotas", mascotas)
                    startActivity(intent)
                }
            }
        }
    }





}