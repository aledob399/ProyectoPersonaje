package com.example.proyectopersonaje


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.speech.tts.TextToSpeech

import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale
import java.util.logging.Handler


class Aventura : AppCompatActivity(),TextToSpeech.OnInitListener {
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var openDrawer: Button
    private lateinit var dbHelper: Database
    private lateinit var textToSpeech:TextToSpeech
    private var cadena: String? =null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
            personaje!!.getMochila()!!.setContenido(dbHelper.obtenerArticulos(idUsuarioAuth).getContenido())
            personaje.getLibro()!!.setContenido(dbHelper.obtenerMagias(idUsuarioAuth))
            mascotas=dbHelper.obtenerMascotas(idUsuarioAuth)
        }
        Log.d("DatosPersonaje", "$personaje")
        Log.d("DatosMascota 1", "${mascotas[0]}")
        val db=Database(this)
        val btnLeerTexto = findViewById<ImageButton>(R.id.btnLeerTexto)
        cadena="Inicio de la aventura,tira del dado"
        btnLeerTexto.setOnClickListener {
            leerDatos(cadena!!)
        }

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
        var pos=intent.getIntExtra("posicion",0)
        musica.seekTo(pos)
        musica.start()
        musica.isLooping = true
        btnMusica.setOnClickListener {
            if(musica.isPlaying){
                pos = musica.currentPosition
                musica.pause()
                musica.isLooping = false
                btnMusica.setImageResource(android.R.drawable.ic_media_pause)
            }else{
                btnMusica.setImageResource(android.R.drawable.ic_media_play)
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

                        if (personaje != null) {
                          //  guardarMascotasEnBaseDeDatos(mascotas!!)
                            //personaje.setNivel(3)
                            dbHelper.borrarArticulos(idUsuarioAuth)
                            dbHelper.borrarMagias(idUsuarioAuth)
                            dbHelper.insertarPersonaje(personaje,idUsuarioAuth)
                            dbHelper.insertarArticulos(personaje.getMochila()!!.getContenido(),idUsuarioAuth)
                            dbHelper.insertarMagias(personaje.getLibro()!!.getContenido(),idUsuarioAuth)

                            Toast.makeText(this, "Personaje actualizado con éxito", Toast.LENGTH_SHORT).show()
                        }

                        dbHelper.borrarMascotas(idUsuarioAuth)
                        dbHelper.insertarMascotas(mascotas,idUsuarioAuth)
                        Toast.makeText(this, "Mascotas actualizadas con éxito", Toast.LENGTH_SHORT).show()


                        return@setNavigationItemSelectedListener true
                    }
                    R.id.two -> {
                        drawerLayout.closeDrawer(GravityCompat.END)
                        // Llamada para actualizar el personaje
                        val intent=Intent(this,DialogFlow::class.java)
                        intent.putExtra("personaje",personaje)
                        intent.putExtra("mochila",personaje!!.getMochila())
                        intent.putParcelableArrayListExtra("mascotas", mascotas)

                        startActivity(intent)

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
                    textToSpeech.stop()
                    val intent = Intent(this,Ciudad::class.java)
                    pos = musica.currentPosition
                    intent.putExtra("posicion",pos)
                    intent.putExtra("personaje",personaje)
                    intent.putExtra("mochila",personaje!!.getMochila())
                    intent.putParcelableArrayListExtra("mascotas", mascotas)
                    if(musica.isPlaying){
                        musica.stop()
                    }
                    startActivity(intent)
                }
                2 -> {

                    btn.setImageResource(R.drawable.dos)
                    val intent = Intent(this,Gacha::class.java)
                    pos = musica.currentPosition
                    intent.putExtra("personaje",personaje)
                    intent.putExtra("mochila",personaje!!.getMochila())
                    intent.putParcelableArrayListExtra("mascotas", mascotas)
                    intent.putExtra("posicion",pos)
                    if(musica.isPlaying){
                        musica.stop()
                    }
                    startActivity(intent)

                }
                3 -> {

                    btn.setImageResource(R.drawable.tres)
                    val intent = Intent(this,Mazmorras::class.java)
                    pos = musica.currentPosition
                    intent.putExtra("personaje",personaje)
                    intent.putExtra("mochila",personaje!!.getMochila())
                    intent.putExtra("posicion",pos)
                    intent.putParcelableArrayListExtra("mascotas", mascotas)
                    if(musica.isPlaying){
                        musica.stop()
                    }
                    startActivity(intent)
                }
                4 -> {

                    btn.setImageResource(R.drawable.cuatro)
                    val intent = Intent(this,Mazmorras::class.java)
                    pos = musica.currentPosition
                    intent.putExtra("personaje",personaje)
                    intent.putExtra("mochila",personaje!!.getMochila())
                    intent.putExtra("posicion",pos)
                    intent.putParcelableArrayListExtra("mascotas", mascotas)
                    if(musica.isPlaying){
                        musica.stop()
                    }
                    startActivity(intent)
                }
                5 -> {

                    btn.setImageResource(R.drawable.cinco)
                    val intent = Intent(this,Mazmorras::class.java)
                    pos = musica.currentPosition
                    intent.putExtra("mochila",personaje!!.getMochila())
                    intent.putExtra("personaje",personaje)
                    intent.putParcelableArrayListExtra("mascotas", mascotas)
                    intent.putExtra("posicion",pos)
                    if(musica.isPlaying){
                        musica.stop()
                    }
                    startActivity(intent)
                }
                6 -> {

                    btn.setImageResource(R.drawable.seis)
                    val intent = Intent(this,Mazmorras::class.java)
                    pos = musica.currentPosition
                    intent.putExtra("personaje",personaje)
                    intent.putExtra("mochila",personaje!!.getMochila())
                    intent.putExtra("posicion",pos)
                    intent.putParcelableArrayListExtra("mascotas", mascotas)
                    if(musica.isPlaying){
                        musica.stop()
                    }
                    startActivity(intent)
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.getDefault())
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Idioma no disponible, establecer un idioma de respaldo
                val backupLocale = Locale.US
                val backupResult = textToSpeech.setLanguage(backupLocale)
                if (backupResult == TextToSpeech.LANG_MISSING_DATA || backupResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Si el idioma de respaldo también está ausente o no es compatible, mostrar un mensaje de error
                    Toast.makeText(this, "El idioma no es compatible con Text-to-Speech", Toast.LENGTH_SHORT).show()
                } else {
                    // Si el idioma de respaldo está configurado correctamente, leer los datos del personaje
                    leerDatos(cadena!!)
                }
            } else {
                // Si el TextToSpeech se inicializa correctamente con el idioma predeterminado, leer los datos del personaje
                leerDatos(cadena!!)
            }
        } else {
            Toast.makeText(this, "Error al inicializar Text-to-Speech", Toast.LENGTH_SHORT).show()
        }


    }




    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun leerDatos(cadena:String) {
        // Obtener los datos del personaje y las mascotas
        val textoParaLeer = "Hola"

        // Verificar si el idioma español está disponible
        val localeSpanish = Locale("es", "ES")
        val result = textToSpeech.setLanguage(localeSpanish)

        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Si el idioma español no está disponible, mostrar un mensaje de error
            //  Toast.makeText(this, "El idioma español no está disponible para Text-to-Speech", Toast.LENGTH_SHORT).show()
        } else {
            // Si el idioma español está disponible, leer el texto proporcionado en español
            textToSpeech.speak(cadena.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onDestroy() {
        // Liberar los recursos del TextToSpeech
        if (textToSpeech != null) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }





}