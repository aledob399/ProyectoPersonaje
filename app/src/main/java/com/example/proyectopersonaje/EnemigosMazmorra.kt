package com.example.proyectopersonaje

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide

class EnemigosMazmorra : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enemigos_mazmorra)
        val personaje=intent.getParcelableExtra<Personaje>("personaje")
        val mascotas = intent.getParcelableArrayListExtra<Mascota>("mascotas")
        val mazmorra=intent.getParcelableExtra<Mazmorra>("mazmorra")
        //val mascotas=ArrayList<Mascota>()
        mascotas!!.add(Mascota("hola",Mascota.tipoMascota.LUZ))
        //  val mazmorra=Mazmorra((1..2).random(), Mazmorra.TipoCondicion.entries[(0..1).random()])
        val enemigos=mazmorra!!.getEnemigos()
        var  imageViewAtaqueAPersonaje = findViewById<ImageView>(R.id.ataqueAPersonaje)
        var  imageViewAtaqueAEnemigo = findViewById<ImageView>(R.id.ataqueAEnemigo)
        var  imageViewAtaqueAMascota = findViewById<ImageView>(R.id.ataqueAMascota) // var animacionAtaque = AnimationUtils.loadAnimation(this, R.anim.animacion_ataque)
        var posicionEnemigo=0
        val vidaPersonajeText=findViewById<TextView>(R.id.vidaPersonaje)
        val vidaEnemigoText=findViewById<TextView>(R.id.vidaEnemigo)
        val vidaMascotaText=findViewById<TextView>(R.id.vidaMascota)
        val btnAtaquePersonaje=findViewById<Button>(R.id.ataqueArma)
        val btnMagiaPersonaje=findViewById<Button>(R.id.ataqueLibro)
        val btnMascotaPersonaje=findViewById<Button>(R.id.ataqueMascota)
        val btnHuir=findViewById<Button>(R.id.huir)
        val barraVidaEnemigo: ProgressBar = findViewById(R.id.barraVidaEnemigo)
        val barraVidaPersonaje: ProgressBar = findViewById(R.id.barraVidaPersonaje)
        val barraVidaMascota: ProgressBar = findViewById(R.id.barraVidaMascota)
        val vidaEspejoMascota=mascotas!![0].getSalud()
        val vidaEspejoPersonaje=personaje!!.getSalud()

        var vidaEspejoEnemigo=enemigos[posicionEnemigo] .getSalud()
        barraVidaEnemigo.max = enemigos[posicionEnemigo].getSalud()
        barraVidaPersonaje.max = personaje!!.getSalud()
        barraVidaMascota.max = mascotas!![0].getSalud()
        barraVidaEnemigo.progress = enemigos[posicionEnemigo].getSalud()
        barraVidaPersonaje.progress = personaje!!.getSalud()
        barraVidaMascota.progress = mascotas!![0].getSalud()
        vidaEnemigoText.text= vidaEspejoEnemigo.toString()  + " / " + enemigos[posicionEnemigo].getSalud()
        vidaPersonajeText.text=vidaEspejoPersonaje.toString() + " / " +personaje.getSalud()
        vidaMascotaText.text=vidaEspejoMascota.toString()+ " / "+mascotas[0].getSalud()

        val gifResource = R.raw.animacion_ataque
        btnAtaquePersonaje.setOnClickListener {
            val ataquePersonaje = personaje.getAtaque()
            vidaEspejoEnemigo -= ataquePersonaje



            imageViewAtaqueAEnemigo.visibility = View.VISIBLE
            Glide.with(this)
                .asGif()
                .load(R.raw.animacion_ataque)
                .into(imageViewAtaqueAEnemigo)

            imageViewAtaqueAEnemigo.postDelayed({
                imageViewAtaqueAEnemigo.visibility = View.GONE
            }, 1000) // Puedes ajustar el tiempo según la duración de tu GIF


            // Actualizar la vida del enemigo y la barra de vida
            vidaEnemigoText.text = vidaEspejoEnemigo.toString() + " / " + enemigos[posicionEnemigo].getSalud()
            barraVidaEnemigo.progress = vidaEspejoEnemigo
            if(vidaEspejoEnemigo<=0){
                posicionEnemigo += 1
                //meter bencidiones y maldiciones,recompensas,popup(al ganar y encontrar cosas,cambiar de rival),disminuir durabilidad,aumentar el daño con las armas,pìllar una random,libros(Magias)
                if(posicionEnemigo<enemigos.size){
                    vidaEnemigoText.text= vidaEspejoEnemigo.toString()  + " / " + enemigos[posicionEnemigo].getSalud()
                    barraVidaEnemigo.max = enemigos[posicionEnemigo].getSalud()
                    barraVidaEnemigo.progress = enemigos[posicionEnemigo].getSalud()
                }else{
                    Toast.makeText(this,"Enhorabuena has completado la mazmorra",Toast.LENGTH_SHORT)
                }


            }
        }





    }

}