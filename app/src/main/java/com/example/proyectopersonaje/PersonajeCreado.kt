package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class PersonajeCreado : AppCompatActivity() ,TextToSpeech.OnInitListener  {

    private lateinit var dbHelper: Database
    private var personaje: Personaje? = null
    private lateinit var datos:TextView
    private var mascotas: ArrayList<Mascota>? = null
    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var textToSpeech:TextToSpeech
    private var cadena: String? =null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_personaje_creado)


        val datos: TextView = findViewById(R.id.datos)
        val img: ImageView = findViewById(R.id.img)
        textToSpeech = TextToSpeech(this, this)
       // var mascotas=intent.getParcelableArrayListExtra<Mascota>("mascotas")
        val idUsuarioAuth = FirebaseAuth.getInstance().currentUser!!.uid.toString()
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        //val objeto1 =
          //  Articulo(Articulo.TipoArticulo.ARMA, Articulo.Nombre.DAGA, 2, 34, R.drawable.moneda,1,Articulo.Rareza.COMUN)
            val btnEmpezar = findViewById<Button>(R.id.btnEmpezar)
        val btnLeerTexto = findViewById<ImageButton>(R.id.btnLeerTexto)

            dbHelper = Database(this)


            val modoRegistro = intent.getBooleanExtra("modoRegistro", false)
            if (modoRegistro) {
                personaje = intent.getParcelableExtra<Personaje>("personaje")
                dbHelper.borrarArticulos(idUsuarioAuth)
                dbHelper.borrarMagias(idUsuarioAuth)
                dbHelper.borrarMascotas(idUsuarioAuth)
                //personaje?.getMochila()?.addArticulo(objeto1)
                mascotas=intent.getParcelableArrayListExtra("mascotas")
                mascotas!!.add(Mascota("Inicial",Mascota.tipoMascota.entries[(0..3).random()]))
                do{
                    val magia=Magia(Magia.TipoMagia.entries[(0..3).random()],Magia.Nombre.entries[(0..11).random()],20)
                    personaje!!.getLibro()!!.aprenderMagia(magia)
                }while (personaje!!.getLibro()!!.getContenido().isEmpty())
                Toast.makeText(this, personaje!!.getLibro()!!.getContenido().toString(), Toast.LENGTH_SHORT).show()
                val objeto1 =
                    Articulo(Articulo.TipoArticulo.ARMA, Articulo.Nombre.ESPADA, 2, 34, R.drawable.objetodos,1,Articulo.Rareza.COMUN)
                val objeto2 =
                    Articulo(Articulo.TipoArticulo.PROTECCION, Articulo.Nombre.ESCUDO, 2, 34, R.drawable.objetocuatro,1,Articulo.Rareza.RARO)

                personaje!!.getMochila()!!.addArticulo(objeto1)
                personaje!!.getMochila()!!.addArticulo(objeto2)
                dbHelper.insertarPersonaje(personaje!!, idUsuarioAuth)
                dbHelper.insertarMascotas(mascotas!!,idUsuarioAuth)
                dbHelper.insertarMagias(personaje!!.getLibro()!!.getContenido(),idUsuarioAuth)
                dbHelper.insertarArticulos(personaje!!.getMochila()!!.getContenido(), idUsuarioAuth)

                Toast.makeText(this, "Personaje insertado exitosamente", Toast.LENGTH_SHORT).show()

            } else {
                val idUsuarioAuth = FirebaseAuth.getInstance().currentUser!!.uid.toString()
                try {

                    personaje = dbHelper.obtenerPersonaje(idUsuarioAuth!!)
                     personaje?.getMochila()?.setContenido(dbHelper.obtenerArticulos(idUsuarioAuth).getContenido())
                    mascotas=dbHelper.obtenerMascotas(idUsuarioAuth!!)
                    personaje!!.getLibro()!!.setContenido(dbHelper.obtenerMagias(idUsuarioAuth))
                    if (personaje == null) {

                        Toast.makeText(this, "No se encontró ningún personaje asociado a este usuario", Toast.LENGTH_SHORT).show()

                    } else {

                    }
                } catch (e: Exception) {
                    // Manejar la excepción
                    e.printStackTrace()
                    Toast.makeText(this, "Error al obtener el personaje: ${e.message}", Toast.LENGTH_SHORT).show()
                    datos.text =e.message

                }
            }

            var mochila=Mochila(100)
           // mochila.setContenido(dbHelper.obtenerArticulos(idUsuarioAuth))


            // Mostrar los datos del personaje y las mascotas

                /*buildString {
                append(personaje.toString())
                mascotas?.let { append(it) }
            }
            datos.text

                 */
            val personajeTxt=personaje.toString()

            var txtMascota=""
            repeat(mascotas!!.size){
                txtMascota += mascotas!!.get(it)?.toString()
            }
            datos.text=(txtMascota +datos.text.toString()+ personajeTxt)
            cadena=datos.text.toString()
            val idImagen = intent.getIntExtra("img", 0)
            img.setImageResource(idImagen)
            btnLeerTexto.setOnClickListener {
                leerDatos(cadena!!)
            }
            textToSpeech.stop()
        Log.d("DatosPersonaje", "$personaje")
        Log.d("DatosMascota 1", "${mascotas!![0]}")

            btnVolver.setOnClickListener {
                dbHelper.borrarMascotas(idUsuarioAuth)
                val intent = Intent(this, CreacionPersonaje::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }

            btnEmpezar.setOnClickListener {
                textToSpeech.stop()
                val intent = Intent(this, Aventura::class.java)
                intent.putExtra("personaje", personaje)
                intent.putParcelableArrayListExtra("mascotas", mascotas)
                intent.putExtra("creado",true)

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

    // Importante: Liberar los recursos del TextToSpeech cuando se detiene la actividad




}
