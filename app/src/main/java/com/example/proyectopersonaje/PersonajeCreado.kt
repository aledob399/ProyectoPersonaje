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
    private var mascotas: ArrayList<Mascota>? = null
    private lateinit var firebaseAuth:FirebaseAuth

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_personaje_creado)


        val datos: TextView = findViewById(R.id.datos)
        val img: ImageView = findViewById(R.id.img)
        val idUsuarioAuth = FirebaseAuth.getInstance().currentUser!!.uid.toString()
        val btnVolver = findViewById<Button>(R.id.btnVolver)
            val btnEmpezar = findViewById<Button>(R.id.btnEmpezar)
            dbHelper = DatabaseHelper(this)

            val modoRegistro = intent.getBooleanExtra("modoRegistro", false)
            if (modoRegistro) {

                personaje = intent.getParcelableExtra<Personaje>("personaje")
                dbHelper.insertarPersonaje(personaje!!, FirebaseAuth.getInstance().currentUser!!.uid.toString())

                Toast.makeText(this, "Personaje insertado exitosamente", Toast.LENGTH_SHORT).show()
                dbHelper.insertarArticulos(personaje!!.getMochila().getContenido(), idUsuarioAuth)
            } else {
                val idUsuarioAuth = FirebaseAuth.getInstance().currentUser!!.uid.toString()
                try {
                    personaje = dbHelper.obtenerPersonaje(idUsuarioAuth!!)
                   personaje?.getMochila()?.setContenido(dbHelper.obtenerArticulos(idUsuarioAuth).getContenido())
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
            personaje!!.getMochila().addArticulo(Articulo(Articulo.TipoArticulo.ARMA,Articulo.Nombre.DAGA,2,3,R.drawable.moneda,1))
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
            datos.text=(datos.text.toString()+ personajeTxt)
            val idImagen = intent.getIntExtra("img", 0)
            img.setImageResource(idImagen)

            btnVolver.setOnClickListener {
                val intent = Intent(this, CreacionPersonaje::class.java)
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




}
