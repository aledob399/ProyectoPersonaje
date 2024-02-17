package com.example.proyectopersonaje

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout

class Contrato : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contrato)
        val personaje=intent.getParcelableExtra<Personaje>("personaje")
        personaje!!.getMochila().setContenido(intent.getParcelableExtra<Mochila>("mochila")!!.getContenido())
        val mascotas = intent.getParcelableArrayListExtra<Mascota>("mascotas")
        val btnContinuar=findViewById<Button>(R.id.btnContinuar)
        val btnQuitarTodos=findViewById<Button>(R.id.btnQuitarTodo)
        val linearLayout = findViewById<LinearLayout>(R.id.objetos)
        val objeto1 =
            Articulo(Articulo.TipoArticulo.ARMA, Articulo.Nombre.ESPADA, 2, 34, R.drawable.objetodos,1,Articulo.Rareza.COMUN)
        val objeto2 =
            Articulo(Articulo.TipoArticulo.PROTECCION, Articulo.Nombre.ESCUDO, 2, 34, R.drawable.objetocuatro,1,Articulo.Rareza.RARO)
        personaje!!.getMochila().addArticulo(objeto1)
        personaje!!.getMochila().addArticulo(objeto2)
        Log.d("DatosPersonaje", "$personaje")
        Log.d("DatosMascota 1", "${mascotas!![0]}")
        val articulosAEliminar=ArrayList<Articulo>()
        for (articulo in personaje!!.getMochila().getContenido()) {
            val imageButton = ImageButton(this)
            imageButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            imageButton.setImageResource(articulo.getUrl()) // Asigna la imagen del artículo
            imageButton.setOnClickListener {
                imageButton.visibility= View.GONE
                articulosAEliminar.add(articulo)
            }
            linearLayout.addView(imageButton)
        }

    }
}