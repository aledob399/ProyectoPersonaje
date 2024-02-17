package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import io.grpc.Context

//bencdiciones y maldiciones y recompensas
//popup al ganar y encontrar cosas,cambiar de rival
//disminuir durabilidad
//meter bencidiones y maldiciones,recompensas,popup(al ganar y encontrar cosas,cambiar de rival),disminuir durabilidad,aumentar el daño con las armas,pìllar una random,libros(Magias)

class EnemigosMazmorra : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enemigos_mazmorra)
        /*
               var personaje=intent.getParcelableExtra<Personaje>("personaje")

               val mascotas = intent.getParcelableArrayListExtra<Mascota>("mascotas")

               val mazmorra=intent.getParcelableExtra<Mazmorra>("mazmorra")
                */

        var personaje= Personaje("julian",Personaje.Raza.Elfo,Personaje.Clase.Brujo,Personaje.EstadoVital.Adulto)
        personaje!!.getMochila().addArticulo(Articulo(Articulo.TipoArticulo.ARMA,Articulo.Nombre.DAGA,1,2,R.drawable.ciudad,1,Articulo.Rareza.RARO))
        personaje!!.getMochila().addArticulo(Articulo(Articulo.TipoArticulo.PROTECCION,Articulo.Nombre.ESCUDO,1,2,R.drawable.ciudad,1,Articulo.Rareza.RARO))
        var i=0
/*
        do{
            personaje!!.equipar(personaje!!.getMochila().getContenido()[i])
            i += 1
        }while(personaje!!.getArma()!=null || personaje.getProteccion()!=null && i<personaje!!.getMochila().getContenido().size)


 */
/*
        personaje!!.equipar(personaje.getMochila().getContenido()[0])
        personaje!!.equipar(personaje.getMochila().getContenido()[1])

 */
        val mascotas=ArrayList<Mascota>()
        val magiasDisponibles = listOf("TORNADO", "VENDAVAL", "HURACAN", "ALIENTO", "DESCARGA", "PROPULSION", "FATUO", "MURO", "TEMBLOR", "TERREMOTO", "SANAR", "CURAR")
        val botonesMagia = listOf<Button>(
            findViewById(R.id.TORNADO),
            findViewById(R.id.VENDAVAL),
            findViewById(R.id.HURACAN),
            findViewById(R.id.ALIENTO),
            findViewById(R.id.DESCARGA),
            findViewById(R.id.PROPULSION),
            findViewById(R.id.FATUO),
            findViewById(R.id.MURO),
            findViewById(R.id.TEMBLOR),
            findViewById(R.id.TERREMOTO),
            findViewById(R.id.SANAR),
            findViewById(R.id.CURAR)
        )
        mascotas!!.add(Mascota("hola",Mascota.tipoMascota.LUZ))
        val mazmorra=Mazmorra((1..2).random(), Mazmorra.TipoCondicion.entries[(0..1).random()])
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
        var vidaEspejoMascota=mascotas!![0].getSalud()
        var vidaEspejoPersonaje=personaje!!.getSalud()
        val inflater = LayoutInflater.from(this@EnemigosMazmorra)
        val popupView = inflater.inflate(R.layout.popup_layout, null)
        val mascotaImg=findViewById<ImageView>(R.id.mascota)
        val layoutMagias = findViewById<LinearLayout>(R.id.magias)


        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // Si el popup debe cerrarse al hacer clic fuera de él
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        val view = inflater.inflate(R.layout.popup_layout, null)
// Configurar la imagen del popup según tus necesidades
        val imageViewPopup = popupView.findViewById<ImageView>(R.id.imageViewPopup)
        val layoutBotonesAbajoDerecha = findViewById<LinearLayout>(R.id.layout_botones_abajo_derecha)
        var vidaEspejoEnemigo=enemigos[posicionEnemigo] .getSalud()
        personaje.getLibro().aprenderMagia(Magia(Magia.TipoMagia.BLANCA,Magia.Nombre.SANAR,2))
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
            if(personaje.getArma()!=null){
                val ataquePersonaje = personaje.getAtaque()+personaje.getArma()!!.getAumentoAtaque()
                vidaEspejoEnemigo -= ataquePersonaje
                personaje.getArma()!!.setDurabilidad(personaje.getArma()!!.getDurabilidad()-1)
            }else{
                val ataquePersonaje = personaje.getAtaque()
                vidaEspejoEnemigo -= ataquePersonaje
            }




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
                vidaEspejoEnemigo=enemigos[posicionEnemigo].getSalud()
                if(posicionEnemigo<enemigos.size){
                    imageViewPopup.setImageResource(R.drawable.nuevoenemigo)
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                    Handler().postDelayed({
                        popupWindow.dismiss()
                    }, 2000)
                    vidaEnemigoText.text= vidaEspejoEnemigo.toString()  + " / " + enemigos[posicionEnemigo].getSalud()
                    barraVidaEnemigo.max = enemigos[posicionEnemigo].getSalud()
                    barraVidaEnemigo.progress = enemigos[posicionEnemigo].getSalud()
                }else{
                    Toast.makeText(this,"Enhorabuena has completado la mazmorra",Toast.LENGTH_SHORT)
                }


            }else{
                Handler().postDelayed({
                    when((1..2).random()){
                        1->{
                            vidaEspejoPersonaje -= enemigos[posicionEnemigo].getAtaque()
                            imageViewAtaqueAPersonaje.visibility = View.VISIBLE
                            Glide.with(this)
                                .asGif()
                                .load(R.raw.animacion_ataque)
                                .into(imageViewAtaqueAPersonaje)

                            imageViewAtaqueAPersonaje.postDelayed({
                                imageViewAtaqueAPersonaje.visibility = View.GONE
                            }, 1000)
                            if(vidaEspejoPersonaje<=0){
                                imageViewPopup.setImageResource(R.drawable.gameover)
                                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                                Handler().postDelayed({
                                    popupWindow.dismiss()
                                }, 2000)
                                val intent=Intent(this,Aventura::class.java)
                                intent.putExtra("personaje",personaje)
                                intent.putParcelableArrayListExtra("mascotas", mascotas)
                                startActivity(intent)
                            }else{
                                vidaPersonajeText.text=vidaEspejoPersonaje.toString()+ " / "+ personaje.getSalud()
                                barraVidaPersonaje.progress=vidaEspejoPersonaje
                            }
                        }
                        2->{
                            vidaEspejoMascota-= enemigos[posicionEnemigo].getAtaque()
                            imageViewAtaqueAMascota.visibility = View.VISIBLE
                            Glide.with(this)
                                .asGif()
                                .load(R.raw.animacion_ataque)
                                .into(imageViewAtaqueAMascota)

                            imageViewAtaqueAMascota.postDelayed({
                                imageViewAtaqueAMascota.visibility = View.GONE
                            }, 1000)
                            if(vidaEspejoMascota<=0){
                                vidaEspejoMascota=0
                                vidaMascotaText.text= vidaEspejoMascota.toString()  + " / " + mascotas[0].getSalud()
                                mascotaImg.setImageResource(R.drawable.lapida)
                                barraVidaMascota.progress =vidaEspejoMascota
                                imageViewPopup.setImageResource(R.drawable.mascotamuerta)

                                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                                Handler().postDelayed({
                                    popupWindow.dismiss()
                                }, 2000)

                            }else{
                                vidaMascotaText.text= vidaEspejoMascota.toString()  + " / " + mascotas[0].getSalud()
                                barraVidaMascota.progress =vidaEspejoMascota
                            }
                        }
                    }
                }, 3000) // Retraso de 2 segundos


            }
        }
        btnMagiaPersonaje.setOnClickListener {
            layoutBotonesAbajoDerecha.visibility = View.GONE
            layoutMagias.visibility=View.VISIBLE
            val magias = personaje.getLibro().getContenido() // Obtener la lista de magias del personaje
            botonesMagia.forEach{boton->
                magias.forEach{magia ->
                    if(boton.text==magia.getNombre().name){
                        boton.visibility=View.VISIBLE
                        boton.setOnClickListener {

                            var daño=personaje.usarMagia(magia.getNombre())
                            when(daño){
                                0->{

                                    //no va bien el currar


                                    Toast.makeText(this, "No puedes usar esa magia", Toast.LENGTH_SHORT).show()}
                                else->{

                                    if(daño==1){
                                        vidaEspejoPersonaje=personaje.getSalud()
                                        imageViewAtaqueAPersonaje.visibility = View.VISIBLE
                                        Glide.with(this)
                                            .asGif()
                                            .load(R.raw.cura)
                                            .into(imageViewAtaqueAPersonaje)

                                        imageViewAtaqueAPersonaje.postDelayed({
                                            imageViewAtaqueAPersonaje.visibility = View.GONE
                                        }, 1000)
                                    }else{


                                        imageViewAtaqueAEnemigo.visibility = View.VISIBLE
                                        Glide.with(this)
                                            .asGif()
                                            .load(R.raw.animacion_ataque)
                                            .into(imageViewAtaqueAEnemigo)

                                        imageViewAtaqueAEnemigo.postDelayed({
                                            imageViewAtaqueAEnemigo.visibility = View.GONE
                                        }, 1000)
                                        vidaEspejoEnemigo -= daño
                                    }
                                    vidaEnemigoText.text = vidaEspejoEnemigo.toString() + " / " + enemigos[posicionEnemigo].getSalud()
                                    barraVidaEnemigo.progress = vidaEspejoEnemigo
                                    if(vidaEspejoEnemigo<=0){
                                        posicionEnemigo += 1
                                        vidaEspejoEnemigo=enemigos[posicionEnemigo].getSalud()
                                        if(posicionEnemigo<enemigos.size){
                                            imageViewPopup.setImageResource(R.drawable.nuevoenemigo)
                                            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                                            Handler().postDelayed({
                                                popupWindow.dismiss()
                                            }, 2000)
                                            vidaEnemigoText.text= vidaEspejoEnemigo.toString()  + " / " + enemigos[posicionEnemigo].getSalud()
                                            barraVidaEnemigo.max = enemigos[posicionEnemigo].getSalud()
                                            barraVidaEnemigo.progress = enemigos[posicionEnemigo].getSalud()
                                        }else{
                                            Toast.makeText(this,"Enhorabuena has completado la mazmorra",Toast.LENGTH_SHORT)
                                        }


                                    }else{
                                        Handler().postDelayed({
                                            when((1..2).random()){
                                                1->{
                                                    vidaEspejoPersonaje -= enemigos[posicionEnemigo].getAtaque()
                                                    imageViewAtaqueAPersonaje.visibility = View.VISIBLE
                                                    Glide.with(this)
                                                        .asGif()
                                                        .load(R.raw.animacion_ataque)
                                                        .into(imageViewAtaqueAPersonaje)

                                                    imageViewAtaqueAPersonaje.postDelayed({
                                                        imageViewAtaqueAPersonaje.visibility = View.GONE
                                                    }, 1000)
                                                    if(vidaEspejoPersonaje<=0){
                                                        imageViewPopup.setImageResource(R.drawable.gameover)
                                                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                                                        Handler().postDelayed({
                                                            popupWindow.dismiss()
                                                        }, 2000)
                                                        val intent=Intent(this,Aventura::class.java)
                                                        intent.putExtra("personaje",personaje)
                                                        intent.putParcelableArrayListExtra("mascotas", mascotas)
                                                        startActivity(intent)
                                                    }else{
                                                        vidaPersonajeText.text=vidaEspejoPersonaje.toString()+ " / "+ personaje.getSalud()
                                                        barraVidaPersonaje.progress=vidaEspejoPersonaje
                                                    }
                                                }
                                                2->{
                                                    vidaEspejoMascota-= enemigos[posicionEnemigo].getAtaque()
                                                    imageViewAtaqueAMascota.visibility = View.VISIBLE
                                                    Glide.with(this)
                                                        .asGif()
                                                        .load(R.raw.animacion_ataque)
                                                        .into(imageViewAtaqueAMascota)

                                                    imageViewAtaqueAMascota.postDelayed({
                                                        imageViewAtaqueAMascota.visibility = View.GONE
                                                    }, 1000)
                                                    if(vidaEspejoMascota<=0){
                                                        vidaEspejoMascota=0
                                                        vidaMascotaText.text= vidaEspejoMascota.toString()  + " / " + mascotas[0].getSalud()
                                                        mascotaImg.setImageResource(R.drawable.lapida)
                                                        barraVidaMascota.progress =vidaEspejoMascota
                                                        imageViewPopup.setImageResource(R.drawable.mascotamuerta)

                                                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                                                        Handler().postDelayed({
                                                            popupWindow.dismiss()
                                                        }, 2000)

                                                    }else{
                                                        vidaMascotaText.text= vidaEspejoMascota.toString()  + " / " + mascotas[0].getSalud()
                                                        barraVidaMascota.progress =vidaEspejoMascota
                                                    }
                                                }
                                            }
                                        }, 3000) // Retraso de 2 segundos


                                    }

                                }
                            }

                            // Aquí puedes agregar la lógica para ejecutar la magia cuando se hace clic en el botón
                            Toast.makeText(this, "Has utilizado la magia: ${magia.getNombre().name}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }


        }





    }

}