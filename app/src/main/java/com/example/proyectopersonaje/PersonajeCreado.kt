package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class PersonajeCreado : AppCompatActivity() {

    private lateinit var dbHelper: Database
    private var personaje: Personaje? = null
    private var mascotas: ArrayList<Mascota>? = null
    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var textToSpeech:TextToSpeech

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_personaje_creado)


        val datos: TextView = findViewById(R.id.datos)
        val img: ImageView = findViewById(R.id.img)
        var mascotas=intent.getParcelableArrayListExtra<Mascota>("mascotas")
        val idUsuarioAuth = FirebaseAuth.getInstance().currentUser!!.uid.toString()
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val objeto1 =
            Articulo(Articulo.TipoArticulo.ARMA, Articulo.Nombre.DAGA, 2, 34, R.drawable.objeto,1,Articulo.Rareza.COMUN)
            val btnEmpezar = findViewById<Button>(R.id.btnEmpezar)
        textToSpeech = TextToSpeech(this){status->
            if (status == TextToSpeech.SUCCESS){
                val result = textToSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA||result == TextToSpeech.LANG_NOT_SUPPORTED){
                    Toast.makeText(this,"lenguaje no soportado",Toast.LENGTH_LONG).show()
                }
            }
        }
            dbHelper = Database(this)
        personaje = intent.getParcelableExtra<Personaje>("personaje")

            val modoRegistro = intent.getBooleanExtra("modoRegistro", false)
            if (modoRegistro) {

                dbHelper.borrarArticulos(idUsuarioAuth)
                dbHelper.borrarArticulos(idUsuarioAuth)
                dbHelper.borrarMascotas(idUsuarioAuth)
               // personaje?.getMochila()?.addArticulo(objeto1)
                mascotas!!.add(Mascota("Inicial",Mascota.tipoMascota.entries[(0..3).random()]))
                do{
                    val magia=Magia(Magia.TipoMagia.entries[(0..3).random()],Magia.Nombre.entries[(0..11).random()],20)
                    personaje!!.getLibro().aprenderMagia(magia)
                }while (personaje!!.getLibro().getContenido().isEmpty())
                Toast.makeText(this, personaje!!.getLibro().getContenido().toString(), Toast.LENGTH_SHORT).show()
                dbHelper.insertarPersonaje(personaje!!, FirebaseAuth.getInstance().currentUser!!.uid.toString())
                dbHelper.insertarMascotas(mascotas,idUsuarioAuth)
                dbHelper.insertarMagias(personaje!!.getLibro().getContenido(),idUsuarioAuth)
                val objeto1 =
                    Articulo(Articulo.TipoArticulo.ARMA, Articulo.Nombre.MARTILLO, 2, 34, R.drawable.objeto,1,Articulo.Rareza.COMUN)

                personaje!!.getMochila().addArticulo(objeto1)
                Toast.makeText(this, "Personaje insertado exitosamente", Toast.LENGTH_SHORT).show()
                dbHelper.insertarArticulos(personaje!!.getMochila().getContenido(), idUsuarioAuth)
            } else {
                val idUsuarioAuth = FirebaseAuth.getInstance().currentUser!!.uid.toString()
                try {
                    personaje = dbHelper.obtenerPersonaje(idUsuarioAuth!!)
                     personaje?.getMochila()?.setContenido(dbHelper.obtenerArticulos(idUsuarioAuth).getContenido())
                    mascotas=dbHelper.obtenerMascotas(idUsuarioAuth!!)
                    personaje!!.getLibro().setContenido(dbHelper.obtenerMagias(idUsuarioAuth))
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

            val idImagen = intent.getIntExtra("img", 0)
            img.setImageResource(idImagen)
            textToSpeech.speak(
            datos.text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
            btnVolver.setOnClickListener {
                dbHelper.borrarMascotas(idUsuarioAuth)
                val intent = Intent(this, CreacionPersonaje::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }

            btnEmpezar.setOnClickListener {
                val intent = Intent(this, Aventura::class.java)
                intent.putExtra("personaje", personaje)
                if(mascotas!=null){
                    intent.putParcelableArrayListExtra("mascotas", mascotas)
                }else{
                    intent.putParcelableArrayListExtra("mascotas", null)
                }


                startActivity(intent)
            }
    }




}
