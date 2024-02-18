package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.util.Locale
import kotlin.random.Random

class Mazmorras : AppCompatActivity(),TextToSpeech.OnInitListener {
    private lateinit var textToSpeech:TextToSpeech
    private var cadena: String? =null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mazmorra)

        val personaje=intent.getParcelableExtra<Personaje>("personaje")

        val mascotas = intent.getParcelableArrayListExtra<Mascota>("mascotas")
        textToSpeech = TextToSpeech(this){status->
            if (status == TextToSpeech.SUCCESS){
                val result = textToSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA||result == TextToSpeech.LANG_NOT_SUPPORTED){
                    Toast.makeText(this,"lenguaje no soportado",Toast.LENGTH_LONG).show()
                }
            }
        }


        val musica: MediaPlayer = MediaPlayer.create(this, R.raw.musica)
        val btnMusica: ImageButton = findViewById(R.id.btnMusica)

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
        val btnEntrar=findViewById<Button>(R.id.entrar)
        val btnContinuar=findViewById<Button>(R.id.continuar)
        val textoMazmorra=findViewById<TextView>(R.id.textoMazmorra)
        val mazmorra=Mazmorra((1..2).random(), Mazmorra.TipoCondicion.entries[(0..1).random()])
        textoMazmorra.text=when(mazmorra.getCondicion()){
            Mazmorra.TipoCondicion.MENOSATAQUE->"Mazmorra con -20% de ataque de dificultad ${mazmorra.getDificultad()} con ${mazmorra.getEnemigos().size} enemigos"
            Mazmorra.TipoCondicion.MENOSVIDA->"Mazmorra con -20% de vida de dificultad ${mazmorra.getDificultad()} con ${mazmorra.getEnemigos().size} enemigos"
            else -> ""
        }
        val btnLeerTexto = findViewById<ImageButton>(R.id.btnLeerTexto)
        cadena="Te has encontrado con una "+textoMazmorra.text.toString()+" ¿que vas a hacer?"

        btnLeerTexto.setOnClickListener {
            leerDatos(cadena!!)
        }
        btnEntrar.setOnClickListener {
            val intent=Intent(this,EnemigosMazmorra::class.java)
            pos = musica.currentPosition


            intent.putParcelableArrayListExtra("mascotas", mascotas)


            intent.putExtra("mazmorra",mazmorra)

            intent.putExtra("personaje",personaje)
            intent.putExtra("posicion",pos)
            if(musica.isPlaying){
                musica.stop()
            }

            startActivity(intent)




        }
        btnContinuar.setOnClickListener {
            val intent=Intent(this,Aventura::class.java)
            pos = musica.currentPosition
            intent.putExtra("personaje",personaje)
            intent.putParcelableArrayListExtra("mascotas", mascotas)
            intent.putExtra("posicion",pos)
            if(musica.isPlaying){
                musica.stop()
            }
            startActivity(intent)
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