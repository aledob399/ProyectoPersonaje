package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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

class Contrato : AppCompatActivity() {
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
        val objeto1 =
            Articulo(Articulo.TipoArticulo.ARMA, Articulo.Nombre.ESPADA, 2, 34, R.drawable.objetodos,1,Articulo.Rareza.COMUN)
        val objeto2 =
            Articulo(Articulo.TipoArticulo.PROTECCION, Articulo.Nombre.ESCUDO, 2, 34, R.drawable.objetocuatro,1,Articulo.Rareza.RARO)
        //personaje!!.getMochila()!!.addArticulo(objeto1)
      //  personaje!!.getMochila()!!.addArticulo(objeto2)
        Log.d("DatosPersonaje", "$personaje")
        Log.d("DatosMascota 1", "${mascotas!![0]}")
        var nuevoArticulo: Articulo? =null
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
            intent.putExtra("personaje",personaje)
            intent.putExtra("mochila",personaje!!.getMochila())
            intent.putParcelableArrayListExtra("mascotas", mascotas)
            startActivity(intent)
        }
        btnContinuar.setOnClickListener {
            val intent= Intent(this,Aventura::class.java)
            intent.putExtra("personaje",personaje)
            intent.putExtra("mochila",personaje!!.getMochila())
            intent.putParcelableArrayListExtra("mascotas", mascotas)
            startActivity(intent)
        }
        btnContratoFinal.setOnClickListener {
            for (a in articulosAEliminar) {
                personaje.getMochila()!!.borrarArticulo(a)
            }
            objetosText.text="Objetos intercambiados con extio"



            val rarezaAleatoria = (cantidadRareza..100).random()
            val rarezaArticulo = when {
                rarezaAleatoria <= 40 -> Articulo.Rareza.COMUN
                rarezaAleatoria <= 70 -> Articulo.Rareza.RARO
                rarezaAleatoria <= 90 -> Articulo.Rareza.EPICO
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

            },3000)

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


}