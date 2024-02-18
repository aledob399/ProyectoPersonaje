package com.example.proyectopersonaje
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.proyectopersonaje.Mascota
import java.util.Locale

class Mascotas : AppCompatActivity(),TextToSpeech.OnInitListener {
    private lateinit var textToSpeech:TextToSpeech
    private var cadena: String? =null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mascotas) // Reemplaza "tu_layout" con el nombre real de tu archivo XML
        val btnContinuar=findViewById<Button>(R.id.continuar)
       // val btnHuevos=findViewById<Button>(R.id.huevos)
        val personaje=intent.getParcelableExtra<Personaje>("personaje")
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
        val mascotas: ArrayList<Mascota>? = intent.getParcelableArrayListExtra("mascotas")
        val cardContainer: LinearLayout = findViewById(R.id.cardContainer)
        val btnLeerTexto = findViewById<ImageButton>(R.id.btnLeerTexto)
        cadena="Estas son tus mascotas"

        btnLeerTexto.setOnClickListener {
            leerDatos(cadena!!)
        }

        btnContinuar.setOnClickListener {
            val intent= Intent(this,Aventura::class.java)
            pos = musica.currentPosition
            intent.putExtra("personaje",personaje)
            intent.putParcelableArrayListExtra("mascotas", mascotas)
            intent.putExtra("posicion",pos)
            if(musica.isPlaying){
                musica.stop()
            }
            startActivity(intent)
        }
        // Verificar si el ArrayList no es nulo y no está vacío
        if (mascotas != null && mascotas.isNotEmpty()) {
            // Iterar sobre cada mascota en el ArrayList
            for (mascota in mascotas) {
                cadena=cadena + mascota.toString()
                val cardView = CardView(this)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(20, 20, 20, 20)
                cardView.layoutParams = layoutParams
                cardView.cardElevation = 8f
                cardView.radius = 16f

                // Crear un nuevo LinearLayout dentro del CardView
                val linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                linearLayout.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                linearLayout.setPadding(20, 20, 20, 20)

                // Crear un ImageView para la imagen de la mascota (asumiendo que tienes la imagen en un recurso drawable)
                val imageView = ImageView(this)
                when(mascota.getAtributo()){
                    Mascota.tipoMascota.AGUA-> imageView.setImageResource(R.drawable.mascota_agua)
                    Mascota.tipoMascota.FUEGO-> imageView.setImageResource(R.drawable.mascota_fuego)
                    Mascota.tipoMascota.OSCURIDAD-> imageView.setImageResource(R.drawable.mascota_oscuridad)
                    Mascota.tipoMascota.LUZ-> imageView.setImageResource(R.drawable.mascota_luz)
                    else -> {imageView.setImageResource(R.drawable.mascota_luz)}
                }
                 // Reemplaza "nombre_imagen" con el nombre real de tu imagen
                val imageLayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                imageView.layoutParams = imageLayoutParams

                // Crear un TextView para mostrar la información de la mascota
                val textView = TextView(this)
                textView.text = mascota.toString() // Aquí debes ajustar cómo quieres mostrar la información de la mascota
                val textLayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                textView.layoutParams = textLayoutParams

                // Agregar la imagen y el TextView al LinearLayout
                linearLayout.addView(imageView)
                linearLayout.addView(textView)

                // Agregar el LinearLayout al CardView
                cardView.addView(linearLayout)

                // Agregar el CardView al contenedor en tu layout
                cardContainer.addView(cardView)
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
