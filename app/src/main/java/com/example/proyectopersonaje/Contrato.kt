package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.util.Locale

class Contrato : AppCompatActivity(),TextToSpeech.OnInitListener {
    private var cadena: String? =null
    private lateinit var textToSpeech:TextToSpeech
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contrato)
        val personaje=intent.getParcelableExtra<Personaje>("personaje")
        //personaje!!.getMochila()!!.setContenido(intent.getParcelableExtra<Mochila>("mochila")!!.getContenido())
        val mascotas = intent.getParcelableArrayListExtra<Mascota>("mascotas")
        val btnContinuar=findViewById<Button>(R.id.btnContinuar)
        val btnQuitarTodos=findViewById<Button>(R.id.btnQuitarTodo)
        val btnContratoFinal=findViewById<Button>(R.id.contratoFinal)
        val objetosText=findViewById<TextView>(R.id.objetosPillados)
        objetosText.text=""
        var cantidadRareza=0
        val recompensaImageView = findViewById<ImageView>(R.id.recompensaContrato)
        val popupView = layoutInflater.inflate(R.layout.popup_recompensa, null)
        val popupWindow = PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true)
        val textViewRecompensa = popupView.findViewById<TextView>(R.id.textViewRecompensa)
        val linearLayout = findViewById<LinearLayout>(R.id.objetos)
        val btnLeerTexto = findViewById<ImageButton>(R.id.btnLeerTexto)
        val objeto1 =
            Articulo(Articulo.TipoArticulo.ARMA, Articulo.Nombre.ESPADA, 2, 34, R.drawable.objetodos,1,Articulo.Rareza.COMUN)
        val objeto2 =
            Articulo(Articulo.TipoArticulo.PROTECCION, Articulo.Nombre.ESCUDO, 2, 34, R.drawable.objetocuatro,1,Articulo.Rareza.RARO)
        //personaje!!.getMochila()!!.addArticulo(objeto1)
      //  personaje!!.getMochila()!!.addArticulo(objeto2)
        Log.d("DatosPersonaje", "$personaje")
        Log.d("DatosMascota 1", "${mascotas!![0]}")
        var nuevoArticulo: Articulo? =null
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
        val articulosAEliminar=ArrayList<Articulo>()
        for (articulo in personaje!!.getMochila()!!.getContenido()) {
            val imageButton = ImageButton(this)
            imageButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            imageButton.setImageResource(articulo.getUrl()) // Asigna la imagen del artículo
            imageButton.setOnClickListener {

                imageButton.visibility= View.GONE
                articulosAEliminar.add(articulo)
                objetosText.text= objetosText.text.toString()+" "+articulo.toString()
                cantidadRareza=cantidadRareza+rarezaPuntos(articulo.getRareza())
            }
            linearLayout.addView(imageButton)
        }
        btnQuitarTodos.setOnClickListener{
            val intent= Intent(this,Contrato::class.java)
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
        btnContinuar.setOnClickListener {
            val intent= Intent(this,Aventura::class.java)
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
        btnContratoFinal.setOnClickListener {
            for (a in articulosAEliminar) {
                personaje.getMochila()!!.borrarArticulo(a)
            }
            objetosText.text="Objetos intercambiados con extio"



            val rarezaAleatoria = (cantidadRareza..100).random()
            val rarezaArticulo = when {
                rarezaAleatoria <= 20 -> Articulo.Rareza.COMUN
                rarezaAleatoria <= 30 -> Articulo.Rareza.RARO
                rarezaAleatoria <= 70 -> Articulo.Rareza.EPICO
                else -> Articulo.Rareza.LEGENDARIO
            }
            var numeroObj=personaje.getMochila()!!.getContenido().size
            do{


            val tipoArticuloAleatorio = Articulo.TipoArticulo.values().random()
            val nombreArticuloAleatorio = Articulo.Nombre.values().random()
            val pesoArticuloAleatorio = (1..10).random()
            val precioArticuloAleatorio = (1..100).random()
            var urlArticuloAleatorio = R.drawable.cinco
            when(nombreArticuloAleatorio){
                Articulo.Nombre.DAGA->urlArticuloAleatorio=R.drawable.objetoocho
                Articulo.Nombre.POCION->urlArticuloAleatorio=R.drawable.objetodos
                Articulo.Nombre.BASTON->urlArticuloAleatorio=R.drawable.objetodos
                Articulo.Nombre.ESPADA->urlArticuloAleatorio=R.drawable.objetodos
                Articulo.Nombre.MARTILLO->urlArticuloAleatorio=R.drawable.objetodos
                Articulo.Nombre.GARRAS->urlArticuloAleatorio=R.drawable.objetodos
                Articulo.Nombre.ESCUDO->urlArticuloAleatorio=R.drawable.objetocuatro
                else -> {urlArticuloAleatorio=R.drawable.objetodos}
            }
                nuevoArticulo = Articulo(tipoArticuloAleatorio, nombreArticuloAleatorio, pesoArticuloAleatorio, precioArticuloAleatorio, urlArticuloAleatorio, 1, rarezaArticulo)
                personaje.getMochila()!!.addArticulo(nuevoArticulo!!)
            }while(numeroObj==personaje.getMochila()!!.getContenido().size)
            textViewRecompensa.text=nuevoArticulo.toString()
            popupWindow.width = LinearLayout.LayoutParams.WRAP_CONTENT
            popupWindow.height = LinearLayout.LayoutParams.WRAP_CONTENT
            recompensaImageView.setImageResource(nuevoArticulo!!.getUrl())
            popupWindow.isOutsideTouchable = true

            popupWindow.contentView = popupView

            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
            recompensaImageView.visibility=View.VISIBLE





            Handler().postDelayed({
                recompensaImageView.visibility=View.GONE
                popupWindow.dismiss()
                Log.d("DatosPersonaje", "$personaje")
                Log.d("DatosMascota 1", "${mascotas!![0]}")
            },3000)

          }
        btnLeerTexto.setOnClickListener {
            cadena=objetosText.text.toString()
            leerDatos(cadena!!)
        }
        }

    private fun rarezaPuntos(rareza:Articulo.Rareza):Int{
        var puntos=0
        when(rareza){
            Articulo.Rareza.COMUN-> puntos += 1
            Articulo.Rareza.RARO->puntos += 3
            Articulo.Rareza.EPICO->puntos += 5
            Articulo.Rareza.LEGENDARIO->puntos += 7
            else -> {puntos += 1}
        }
        return puntos
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