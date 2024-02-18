package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.view.LayoutInflater

import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import java.util.Locale

//bencdiciones y maldiciones y recompensas
//popup al ganar y encontrar cosas,cambiar de rival
//disminuir durabilidad
//meter bencidiones y maldiciones,recompensas,popup(al ganar y encontrar cosas,cambiar de rival),disminuir durabilidad,aumentar el daño con las armas,pìllar una random,libros(Magias)

class EnemigosMazmorra : AppCompatActivity(),TextToSpeech.OnInitListener {
    private lateinit var textToSpeech:TextToSpeech
    private var cadena: String? =null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_enemigos_mazmorra)

                   var personaje=intent.getParcelableExtra<Personaje>("personaje")

                   val mascotas = intent.getParcelableArrayListExtra<Mascota>("mascotas")

                   val mazmorra=intent.getParcelableExtra<Mazmorra>("mazmorra")
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

           // var personaje= Personaje("julian",Personaje.Raza.Elfo,Personaje.Clase.Brujo,Personaje.EstadoVital.Adulto)
           // personaje.setNivel(9)
           // personaje!!.getMochila().addArticulo(Articulo(Articulo.TipoArticulo.ARMA,Articulo.Nombre.DAGA,1,2,R.drawable.ciudad,1,Articulo.Rareza.RARO))
           // personaje!!.getMochila().addArticulo(Articulo(Articulo.TipoArticulo.PROTECCION,Articulo.Nombre.ESCUDO,1,2,R.drawable.ciudad,1,Articulo.Rareza.RARO))
           // personaje!!.getMochila().addArticulo(Articulo(Articulo.TipoArticulo.ORO,Articulo.Nombre.MONEDA,1,15,R.drawable.ciudad,1,Articulo.Rareza.RARO))
           // personaje!!.getMochila().addArticulo(Articulo(Articulo.TipoArticulo.ORO,Articulo.Nombre.MONEDA,1,15,R.drawable.ciudad,1,Articulo.Rareza.RARO))
           // personaje!!.getMochila().addArticulo(Articulo(Articulo.TipoArticulo.ORO,Articulo.Nombre.MONEDA,1,15,R.drawable.ciudad,1,Articulo.Rareza.RARO))
           // personaje!!.getMochila().addArticulo(Articulo(Articulo.TipoArticulo.ORO,Articulo.Nombre.MONEDA,1,15,R.drawable.ciudad,1,Articulo.Rareza.RARO))
           // personaje!!.getMochila().addArticulo(Articulo(Articulo.TipoArticulo.ORO,Articulo.Nombre.MONEDA,1,15,R.drawable.ciudad,1,Articulo.Rareza.RARO))
           // personaje!!.getMochila().addArticulo(Articulo(Articulo.TipoArticulo.ORO,Articulo.Nombre.MONEDA,1,15,R.drawable.ciudad,1,Articulo.Rareza.RARO))
           // personaje!!.getMochila().addArticulo(Articulo(Articulo.TipoArticulo.ORO,Articulo.Nombre.MONEDA,1,15,R.drawable.ciudad,1,Articulo.Rareza.RARO))
           // personaje!!.getMochila().addArticulo(Articulo(Articulo.TipoArticulo.ORO,Articulo.Nombre.MONEDA,1,15,R.drawable.ciudad,1,Articulo.Rareza.RARO))
            //personaje!!.getMochila().addArticulo(Articulo(Articulo.TipoArticulo.ORO,Articulo.Nombre.MONEDA,1,15,R.drawable.ciudad,1,Articulo.Rareza.RARO))
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
           // val mascotas=ArrayList<Mascota>()
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
           // mascotas!!.add(Mascota("hola",Mascota.tipoMascota.LUZ))
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
            val btnAtras=findViewById<Button>(R.id.btnAtras)
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
            val ataqueMascota=mascotas[0].getAtaque()
            var mascotaMuerta=false

            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true // Si el popup debe cerrarse al hacer clic fuera de él
            val popupWindow = PopupWindow(popupView, width, height, focusable)
            val view = inflater.inflate(R.layout.popup_layout, null)
    // Configurar la imagen del popup según tus necesidades
            val imageViewPopup = popupView.findViewById<ImageView>(R.id.imageViewPopup)
            val layoutBotonesAbajoDerecha = findViewById<LinearLayout>(R.id.layout_botones_abajo_derecha)
            var vidaEspejoEnemigo=enemigos[posicionEnemigo] .getSalud()
            var ataquePersonaje=personaje.getAtaque()
            var vidaMaxPersonaje=personaje.getSalud()
            vidaEspejoPersonaje=vidaMaxPersonaje
            val btnLeerTexto = findViewById<ImageButton>(R.id.btnLeerTexto)


            btnLeerTexto.setOnClickListener {
                if(personaje.getArma()==null){
                    cadena="Te quedan ${mazmorra.getEnemigos().size-posicionEnemigo} enemigos para acabar la mazmorra,tienes ${personaje.getMana()} de mana restante "
                }else{
                    cadena="Te quedan ${mazmorra.getEnemigos().size-posicionEnemigo} enemigos para acabar la mazmorra,tienes ${personaje.getMana()} de mana restante y te quedan ${
                        personaje.getArma()!!.getDurabilidad()} utilidades de tu arma"
                }

                leerDatos(cadena!!)
            }
            if(mazmorra.getCondicion()==Mazmorra.TipoCondicion.MENOSATAQUE){
                ataquePersonaje= (ataquePersonaje*0.8).toInt()
            }
            if(mazmorra.getCondicion()==Mazmorra.TipoCondicion.MENOSVIDA){
                vidaEspejoPersonaje= (vidaEspejoPersonaje*0.8).toInt()
                vidaMaxPersonaje=(vidaMaxPersonaje*0.8).toInt()
            }
            //personaje.getLibro().aprenderMagia(Magia(Magia.TipoMagia.BLANCA,Magia.Nombre.SANAR,2))
           // personaje.getLibro().aprenderMagia(Magia(Magia.TipoMagia.AIRE,Magia.Nombre.HURACAN,2))
           // personaje.getLibro().aprenderMagia(Magia(Magia.TipoMagia.FUEGO,Magia.Nombre.HURACAN,2))
           // personaje.getLibro().aprenderMagia(Magia(Magia.TipoMagia.TIERRA,Magia.Nombre.HURACAN,2))
            barraVidaEnemigo.max = enemigos[posicionEnemigo].getSalud()
            barraVidaPersonaje.max = personaje!!.getSalud()
            barraVidaMascota.max = mascotas!![0].getSalud()
            barraVidaEnemigo.progress = enemigos[posicionEnemigo].getSalud()
            barraVidaPersonaje.progress = personaje!!.getSalud()
            barraVidaMascota.progress = mascotas!![0].getSalud()
            vidaEnemigoText.text= vidaEspejoEnemigo.toString()  + " / " + enemigos[posicionEnemigo].getSalud()
            vidaPersonajeText.text=vidaEspejoPersonaje.toString() + " / " +vidaMaxPersonaje
            vidaMascotaText.text=vidaEspejoMascota.toString()+ " / "+mascotas[0].getSalud()

            var flagMaldicionMascota=false
            if(personaje.getArma()!=null){
                ataquePersonaje = +personaje.getArma()!!.getAumentoAtaque()

            }
            val gifResource = R.raw.animacion_ataque
            btnAtaquePersonaje.setOnClickListener {
                if(personaje.getArma()!=null){
                    personaje.getArma()!!.setDurabilidad(personaje.getArma()!!.getDurabilidad()-1)
                }
                for(m in mazmorra.obtenerMaldiciones()){
                    if(m==Mazmorra.TipoMaldicion.ATAQUE){
                        ataquePersonaje= (ataquePersonaje*0.8).toInt()
                    }
                    if(m==Mazmorra.TipoMaldicion.MASCOTA){
                        flagMaldicionMascota=true
                    }
                    if(m==Mazmorra.TipoMaldicion.VIDA){
                        vidaEspejoPersonaje= (vidaEspejoPersonaje*0.8).toInt()
                        vidaMaxPersonaje= (vidaMaxPersonaje*0.8).toInt()
                        vidaPersonajeText.text=vidaEspejoPersonaje.toString()+ " / "+vidaMaxPersonaje
                        barraVidaPersonaje.progress=vidaEspejoPersonaje
                        barraVidaPersonaje.max=vidaMaxPersonaje
                    }
                }
                mazmorra.borrarMaldiciones()
                for(b in mazmorra.obtenerBendiciones()){
                    if(b==Mazmorra.TipoBendicion.ATAQUE){
                        ataquePersonaje= (ataquePersonaje*1.2).toInt()
                    }
                    if(b==Mazmorra.TipoBendicion.ATAQUEDOBLE){
                        ataquePersonaje= (ataquePersonaje*2).toInt()
                    }
                    if(b==Mazmorra.TipoBendicion.VIDA){
                        vidaMaxPersonaje= (vidaMaxPersonaje*2).toInt()
                        vidaEspejoPersonaje= (vidaEspejoPersonaje*2).toInt()
                        vidaPersonajeText.text=vidaEspejoPersonaje.toString()+ " / "+vidaMaxPersonaje
                        barraVidaPersonaje.progress=vidaEspejoPersonaje
                        barraVidaPersonaje.max=vidaMaxPersonaje
                    }
                }
                mazmorra.borrarBendiciones()

                vidaEspejoEnemigo -= ataquePersonaje





                imageViewAtaqueAEnemigo.visibility = View.VISIBLE
                Glide.with(this)
                    .asGif()
                    .load(R.raw.animacion_ataque)
                    .into(imageViewAtaqueAEnemigo)

                imageViewAtaqueAEnemigo.postDelayed({
                    imageViewAtaqueAEnemigo.visibility = View.GONE
                }, 1000)



                vidaEnemigoText.text = vidaEspejoEnemigo.toString() + " / " + enemigos[posicionEnemigo].getSalud()
                barraVidaEnemigo.progress = vidaEspejoEnemigo
                if(vidaEspejoEnemigo<=0){
                    val popupView = layoutInflater.inflate(R.layout.popup_recompensa, null)

                    val textViewRecompensa = popupView.findViewById<TextView>(R.id.textViewRecompensa)
                    when((1..5).random()){
                        1->{
                            val mas =Mascota("Mascota",Mascota.tipoMascota.entries[(0..1).random()])
                            textViewRecompensa.text = "¡Has derrotado a un enemigo! Has recibido una mascota $mas"

                            mascotas.add(mas)
                        }
                        2->{



                            var magia: Magia? =null
                            var p= Personaje("julian",Personaje.Raza.Elfo,Personaje.Clase.Brujo,Personaje.EstadoVital.Adulto,Libro(4,4,4,4,ArrayList<Magia>()),
                                Mochila(100)
                            )
                            do{
                                 magia=Magia(Magia.TipoMagia.entries[(0..3).random()],Magia.Nombre.entries[(0..11).random()],20)
                                p!!.getLibro()!!.aprenderMagia(magia)
                            }while (p!!.getLibro()!!.getContenido().isEmpty())
                            personaje.getLibro()!!.aprenderMagia(magia!!)
                            textViewRecompensa.text = "¡Has derrotado a un enemigo! Has desbloqueado una nueva magia: ${magia}"

                        }
                        3->{
                            var p= Personaje("julian",Personaje.Raza.Elfo,Personaje.Clase.Brujo,Personaje.EstadoVital.Adulto,Libro(4,4,4,4,ArrayList<Magia>()),
                                Mochila(100)
                            )
                            var arti: Articulo? =null
                            do{
                                arti=Articulo(Articulo.TipoArticulo.entries[(0..3).random()],Articulo.Nombre.entries[(0..9).random()],(0..9).random(),(0..45).random(),R.drawable.moneda,1,Articulo.Rareza.entries[(0..3).random()])
                                p.getMochila()!!.addArticulo(arti)
                            }while(p.getMochila()!!.getContenido().isEmpty())
                            val n=personaje.getMochila()!!.getContenido().size

                            personaje.getMochila()!!.addArticulo(arti!!)
                            if(personaje.getMochila()!!.getContenido().size>n){
                                textViewRecompensa.text = "¡Has derrotado a un enemigo! Has conseguido un nuevo objeto: ${arti}"
                            }else{
                                textViewRecompensa.text = "¡Has derrotado a un enemigo! No puededes meter mas objetos en la mochila"
                            }


                        }
                        4->{
                            val maldicion=Mazmorra.TipoMaldicion.entries[(0..3).random()]
                            mazmorra.agregarMaldicion(maldicion)
                            textViewRecompensa.text = "¡Has derrotado a un enemigo! Te ha caido una maldicion: ${maldicion.name}"

                        }
                        5->{
                            val bendicion=Mazmorra.TipoBendicion.entries[(0..3).random()]
                            mazmorra.agregarBendicion(bendicion)
                            textViewRecompensa.text = "¡Has derrotado a un enemigo! Has conseguido una nueva bendicion: ${bendicion.name}"
                        }

                    }


                    popupWindow.width = LinearLayout.LayoutParams.WRAP_CONTENT
                    popupWindow.height = LinearLayout.LayoutParams.WRAP_CONTENT

                    popupWindow.isOutsideTouchable = true

                    popupWindow.contentView = popupView

                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
                    posicionEnemigo += 1
                    vidaEspejoEnemigo=enemigos[posicionEnemigo].getSalud()
                    if(posicionEnemigo<enemigos.size){

                        imageViewPopup.visibility=View.VISIBLE
                        imageViewPopup.setImageResource(R.drawable.nuevoenemigo)
                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                        Handler().postDelayed({
                            popupWindow.dismiss()

                        }, 3000)
                        vidaEnemigoText.text= vidaEspejoEnemigo.toString()  + " / " + enemigos[posicionEnemigo].getSalud()
                        barraVidaEnemigo.max = enemigos[posicionEnemigo].getSalud()
                        barraVidaEnemigo.progress = enemigos[posicionEnemigo].getSalud()
                    }else{



                        Toast.makeText(this,"Enhorabuena has completado la mazmorra,volveras al inicio",Toast.LENGTH_SHORT)

                        Handler().postDelayed({


                            val intent=Intent(this,Aventura::class.java)
                            pos = musica.currentPosition
                            intent.putExtra("personaje",personaje)
                            intent.putParcelableArrayListExtra("mascotas", mascotas)
                            intent.putExtra("posicion",pos)
                            startActivity(intent)


                        }, 3000)


                    }


                }else{
                    Handler().postDelayed({
                        var num=(1..2).random()
                        if(vidaEspejoMascota<=0){
                            num=1
                        }
                        when(num){
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
                                    val popupView = layoutInflater.inflate(R.layout.popup_layout, null)
                                    val imageView = popupView.findViewById<ImageView>(R.id.popUp)
                                    imageView.setImageResource(R.drawable.gameover)

                                    val popupWindow = PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true)
                                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

                                    Handler().postDelayed({
                                        popupWindow.dismiss()
                                        val intent = Intent(this, Aventura::class.java)
                                        pos = musica.currentPosition
                                        intent.putExtra("personaje", personaje)
                                        intent.putParcelableArrayListExtra("mascotas", mascotas)
                                        intent.putExtra("posicion",pos)
                                        if(musica.isPlaying){
                                            musica.stop()
                                        }
                                        startActivity(intent)
                                    }, 2000)

                                }else{
                                    vidaPersonajeText.text=vidaEspejoPersonaje.toString()+ " / "+ vidaMaxPersonaje
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
                                    mascotaMuerta=true
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
                    }, 2000) // Retraso de 2 segundos


                }
            }
            btnMagiaPersonaje.setOnClickListener {
                btnAtras.visibility=View.VISIBLE
                btnAtras.setOnClickListener {
                    layoutBotonesAbajoDerecha.visibility = View.VISIBLE
                    layoutMagias.visibility=View.GONE
                    btnAtras.visibility=View.GONE
                }
                layoutBotonesAbajoDerecha.visibility = View.GONE
                layoutMagias.visibility=View.VISIBLE
                for(m in mazmorra.obtenerMaldiciones()){
                    if(m==Mazmorra.TipoMaldicion.ATAQUE){
                        ataquePersonaje= (ataquePersonaje*0.8).toInt()
                    }
                    if(m==Mazmorra.TipoMaldicion.MASCOTA){
                        flagMaldicionMascota=true
                    }
                    if(m==Mazmorra.TipoMaldicion.VIDA){
                        vidaEspejoPersonaje= (vidaEspejoPersonaje*0.8).toInt()
                        vidaMaxPersonaje= (vidaMaxPersonaje*0.8).toInt()
                        vidaPersonajeText.text=vidaEspejoPersonaje.toString()+ " / "+vidaMaxPersonaje
                        barraVidaPersonaje.progress=vidaEspejoEnemigo
                        barraVidaPersonaje.max=vidaMaxPersonaje
                    }
                }
                mazmorra.borrarMaldiciones()
                for(b in mazmorra.obtenerBendiciones()){
                    if(b==Mazmorra.TipoBendicion.ATAQUE){
                        ataquePersonaje= (ataquePersonaje*1.2).toInt()
                    }
                    if(b==Mazmorra.TipoBendicion.ATAQUEDOBLE){
                        ataquePersonaje= (ataquePersonaje*2).toInt()
                    }
                    if(b==Mazmorra.TipoBendicion.VIDA){
                        vidaEspejoPersonaje= (vidaEspejoPersonaje*2).toInt()
                        vidaMaxPersonaje= (vidaMaxPersonaje*2).toInt()
                        vidaPersonajeText.text=vidaEspejoPersonaje.toString()+ " / "+vidaMaxPersonaje
                        barraVidaPersonaje.progress=vidaEspejoPersonaje
                        barraVidaPersonaje.max=vidaMaxPersonaje
                    }
                }
                mazmorra.borrarBendiciones()
                val magias = personaje.getLibro()!!.getContenido()
                botonesMagia.forEach{boton->
                    magias.forEach{magia ->
                        if(boton.text==magia.getNombre().name){
                            boton.visibility=View.VISIBLE
                            boton.setOnClickListener {
                                layoutBotonesAbajoDerecha.visibility = View.VISIBLE
                                layoutMagias.visibility=View.GONE
                                var daño=personaje.usarMagia(magia.getNombre())
                                when(daño){
                                    (-1)->{}
                                    0->{


                                        imageViewAtaqueAPersonaje.visibility = View.VISIBLE
                                        Glide.with(this)
                                            .asGif()
                                            .load(R.raw.cura)
                                            .into(imageViewAtaqueAPersonaje)

                                        imageViewAtaqueAPersonaje.postDelayed({
                                            imageViewAtaqueAPersonaje.visibility = View.GONE
                                        }, 1000)
                                        vidaEspejoPersonaje=vidaMaxPersonaje
                                        vidaPersonajeText.text = vidaPersonajeText.toString() + " / " + vidaMaxPersonaje
                                        barraVidaPersonaje.progress = vidaEspejoPersonaje
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
                                                var num=(1..2).random()
                                                if(vidaEspejoMascota<=0){
                                                    num=1
                                                }
                                                when(num){
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
                                                                val intent=Intent(this,Aventura::class.java)
                                                                pos = musica.currentPosition
                                                                intent.putExtra("personaje",personaje)
                                                                intent.putParcelableArrayListExtra("mascotas", mascotas)
                                                                intent.putExtra("posicion",pos)
                                                                if(musica.isPlaying){
                                                                    musica.stop()
                                                                }
                                                                startActivity(intent)
                                                            }, 2000)

                                                        }else{
                                                            vidaPersonajeText.text=vidaEspejoPersonaje.toString()+ " / "+ vidaMaxPersonaje
                                                            barraVidaPersonaje.progress=vidaEspejoPersonaje
                                                            barraVidaPersonaje.max=vidaMaxPersonaje
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
                                                            mascotaMuerta=true
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
                                            }, 2000) // Retraso de 2 segundos


                                        }

                                        }
                                    else->{



                                            imageViewAtaqueAEnemigo.visibility = View.VISIBLE
                                            Glide.with(this)
                                                .asGif()
                                                .load(R.raw.animacion_ataque)
                                                .into(imageViewAtaqueAEnemigo)

                                            imageViewAtaqueAEnemigo.postDelayed({
                                                imageViewAtaqueAEnemigo.visibility = View.GONE
                                            }, 1000)
                                            vidaEspejoEnemigo -= daño

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
                                                var num=(1..2).random()
                                                if(vidaEspejoMascota<=0){
                                                    num=1
                                                }
                                                when(num){
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
                                                                val intent=Intent(this,Aventura::class.java)
                                                                pos = musica.currentPosition
                                                                intent.putExtra("personaje",personaje)
                                                                intent.putParcelableArrayListExtra("mascotas", mascotas)
                                                                intent.putExtra("posicion",pos)
                                                                if(musica.isPlaying){
                                                                    musica.stop()
                                                                }
                                                                startActivity(intent)
                                                            }, 2000)

                                                        }else{
                                                            vidaPersonajeText.text=vidaEspejoPersonaje.toString()+ " / "+ vidaMaxPersonaje
                                                            barraVidaPersonaje.progress=vidaEspejoPersonaje
                                                            barraVidaPersonaje.max=vidaMaxPersonaje
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
                                                            mascotaMuerta=true
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

                                Toast.makeText(this, "Has utilizado la magia: ${magia.getNombre().name}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }


            }
            var flag=false
            btnHuir.setOnClickListener {
                val pagar=(1..100).random()
                if(flag==false){
                    val popupView = layoutInflater.inflate(R.layout.popup_recompensa, null)
                    val textViewRecompensa = popupView.findViewById<TextView>(R.id.textViewRecompensa)

                    textViewRecompensa.text="Para huir tendras que  pagar $pagar monedas,vuelve a hacer clic en huir para pagar"
                    popupWindow.width = LinearLayout.LayoutParams.WRAP_CONTENT
                    popupWindow.height = LinearLayout.LayoutParams.WRAP_CONTENT

                    popupWindow.isOutsideTouchable = true

                    popupWindow.contentView = popupView

                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
                    flag=true
                    Handler().postDelayed({
                        popupWindow.dismiss()
                    }, 2500)

                }else{
                    if(personaje.misMonedas()<pagar){
                        val popupView = layoutInflater.inflate(R.layout.popup_recompensa, null)
                        val textViewRecompensa = popupView.findViewById<TextView>(R.id.textViewRecompensa)

                        textViewRecompensa.text="No puedes escapar,solo tienes ${personaje.misMonedas().toString()} y necesitas $pagar"
                        popupWindow.width = LinearLayout.LayoutParams.WRAP_CONTENT
                        popupWindow.height = LinearLayout.LayoutParams.WRAP_CONTENT

                        popupWindow.isOutsideTouchable = true

                        popupWindow.contentView = popupView

                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
                        Handler().postDelayed({
                            popupWindow.dismiss()
                        }, 2500)
                    }else{
                        personaje.restarMonedas(pagar)

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

            }
            btnMascotaPersonaje.setOnClickListener {
                if(!mascotaMuerta){

                    vidaEspejoEnemigo=vidaEspejoEnemigo-ataqueMascota
                    vidaEnemigoText.text=vidaEspejoEnemigo.toString()+" / "+enemigos[posicionEnemigo].getSalud()
                    barraVidaEnemigo.progress=vidaEspejoEnemigo
                    imageViewAtaqueAEnemigo.visibility = View.VISIBLE
                    Glide.with(this)
                        .asGif()
                        .load(R.raw.animacion_ataque)
                        .into(imageViewAtaqueAEnemigo)

                    imageViewAtaqueAEnemigo.postDelayed({
                        imageViewAtaqueAEnemigo.visibility = View.GONE
                    }, 1000)
                    if(vidaEspejoEnemigo<=0){
                        val popupView = layoutInflater.inflate(R.layout.popup_recompensa, null)

                        val textViewRecompensa = popupView.findViewById<TextView>(R.id.textViewRecompensa)
                        when((1..5).random()){
                            1->{
                                val mas =Mascota("Mascota",Mascota.tipoMascota.entries[(0..1).random()])
                                textViewRecompensa.text = "¡Has derrotado a un enemigo! Has recibido una mascota $mas"

                                mascotas.add(mas)
                            }
                            2->{



                                var magia: Magia? =null
                                var p= Personaje("julian",Personaje.Raza.Elfo,Personaje.Clase.Brujo,Personaje.EstadoVital.Adulto,Libro(4,4,4,4,ArrayList<Magia>()),Mochila(100))
                                do{
                                    magia=Magia(Magia.TipoMagia.entries[(0..3).random()],Magia.Nombre.entries[(0..11).random()],20)
                                    p!!.getLibro()!!.aprenderMagia(magia!!)
                                }while (p!!.getLibro()!!.getContenido().isEmpty())
                                personaje.getLibro()!!.aprenderMagia(magia!!)
                                textViewRecompensa.text = "¡Has derrotado a un enemigo! Has desbloqueado una nueva magia: ${magia}"

                            }
                            3->{
                                var p= Personaje("julian",Personaje.Raza.Elfo,Personaje.Clase.Brujo,Personaje.EstadoVital.Adulto,Libro(4,4,4,4,ArrayList<Magia>()),Mochila(100))
                                var arti: Articulo? =null
                                do{
                                    arti=Articulo(Articulo.TipoArticulo.entries[(0..3).random()],Articulo.Nombre.entries[(0..9).random()],(0..9).random(),(0..45).random(),R.drawable.moneda,1,Articulo.Rareza.entries[(0..3).random()])
                                    p.getMochila()!!.addArticulo(arti!!)
                                }while(p.getMochila()!!.getContenido().isEmpty())
                                val n=personaje.getMochila()!!.getContenido().size

                                personaje.getMochila()!!.addArticulo(arti!!)
                                if(personaje.getMochila()!!.getContenido().size>n){
                                    textViewRecompensa.text = "¡Has derrotado a un enemigo! Has conseguido un nuevo objeto: ${arti}"
                                }else{
                                    textViewRecompensa.text = "¡Has derrotado a un enemigo! No puededes meter mas objetos en la mochila"
                                }


                            }
                            4->{
                                val maldicion=Mazmorra.TipoMaldicion.entries[(0..3).random()]
                                mazmorra.agregarMaldicion(maldicion)
                                textViewRecompensa.text = "¡Has derrotado a un enemigo! Te ha caido una maldicion: ${maldicion.name}"

                            }
                            5->{
                                val bendicion=Mazmorra.TipoBendicion.entries[(0..3).random()]
                                mazmorra.agregarBendicion(bendicion)
                                textViewRecompensa.text = "¡Has derrotado a un enemigo! Has conseguido una nueva bendicion: ${bendicion.name}"
                            }

                        }


                        popupWindow.width = LinearLayout.LayoutParams.WRAP_CONTENT
                        popupWindow.height = LinearLayout.LayoutParams.WRAP_CONTENT

                        popupWindow.isOutsideTouchable = true

                        popupWindow.contentView = popupView

                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
                        posicionEnemigo += 1
                        vidaEspejoEnemigo=enemigos[posicionEnemigo].getSalud()
                        if(posicionEnemigo<enemigos.size){

                            imageViewPopup.visibility=View.VISIBLE
                            imageViewPopup.setImageResource(R.drawable.nuevoenemigo)
                            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                            Handler().postDelayed({
                                popupWindow.dismiss()

                            }, 3000)
                            vidaEnemigoText.text= vidaEspejoEnemigo.toString()  + " / " + enemigos[posicionEnemigo].getSalud()
                            barraVidaEnemigo.max = enemigos[posicionEnemigo].getSalud()
                            barraVidaEnemigo.progress = enemigos[posicionEnemigo].getSalud()
                        }else{



                            Toast.makeText(this,"Enhorabuena has completado la mazmorra,volveras al inicio",Toast.LENGTH_SHORT)

                            Handler().postDelayed({


                                val intent=Intent(this,Aventura::class.java)
                                pos = musica.currentPosition
                                intent.putExtra("personaje",personaje)
                                intent.putParcelableArrayListExtra("mascotas", mascotas)
                                intent.putExtra("posicion",pos)
                                if(musica.isPlaying){
                                    musica.stop()
                                }
                                startActivity(intent)


                            }, 3000)


                        }


                    }else {
                        Handler().postDelayed({
                            var num = (1..2).random()
                            if (vidaEspejoMascota <= 0) {
                                num = 1
                            }
                            when (num) {
                                1 -> {
                                    vidaEspejoPersonaje -= enemigos[posicionEnemigo].getAtaque()
                                    imageViewAtaqueAPersonaje.visibility = View.VISIBLE
                                    Glide.with(this)
                                        .asGif()
                                        .load(R.raw.animacion_ataque)
                                        .into(imageViewAtaqueAPersonaje)

                                    imageViewAtaqueAPersonaje.postDelayed({
                                        imageViewAtaqueAPersonaje.visibility = View.GONE
                                    }, 1000)
                                    if (vidaEspejoPersonaje <= 0) {
                                        imageViewPopup.setImageResource(R.drawable.gameover)
                                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                                        Handler().postDelayed({
                                            popupWindow.dismiss()
                                            val intent = Intent(this, Aventura::class.java)
                                            pos = musica.currentPosition
                                            intent.putExtra("personaje", personaje)
                                            intent.putParcelableArrayListExtra("mascotas", mascotas)
                                            intent.putExtra("posicion",pos)
                                            if(musica.isPlaying){
                                                musica.stop()
                                            }
                                            startActivity(intent)
                                        }, 2000)

                                    } else {
                                        vidaPersonajeText.text =
                                            vidaEspejoPersonaje.toString() + " / " + vidaMaxPersonaje
                                        barraVidaPersonaje.progress = vidaEspejoPersonaje
                                    }
                                }

                                2 -> {
                                    vidaEspejoMascota -= enemigos[posicionEnemigo].getAtaque()
                                    imageViewAtaqueAMascota.visibility = View.VISIBLE
                                    Glide.with(this)
                                        .asGif()
                                        .load(R.raw.animacion_ataque)
                                        .into(imageViewAtaqueAMascota)

                                    imageViewAtaqueAMascota.postDelayed({
                                        imageViewAtaqueAMascota.visibility = View.GONE
                                    }, 1000)
                                    if (vidaEspejoMascota <= 0) {
                                        mascotaMuerta = true
                                        vidaEspejoMascota = 0
                                        vidaMascotaText.text =
                                            vidaEspejoMascota.toString() + " / " + mascotas[0].getSalud()
                                        mascotaImg.setImageResource(R.drawable.lapida)
                                        barraVidaMascota.progress = vidaEspejoMascota
                                        imageViewPopup.setImageResource(R.drawable.mascotamuerta)

                                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                                        Handler().postDelayed({
                                            popupWindow.dismiss()
                                        }, 2000)

                                    } else {
                                        vidaMascotaText.text =
                                            vidaEspejoMascota.toString() + " / " + mascotas[0].getSalud()
                                        barraVidaMascota.progress = vidaEspejoMascota
                                    }
                                }
                            }
                        }, 2000) // Retraso de 2 segundos

                    }

                    }else{
                    val popupView = layoutInflater.inflate(R.layout.popup_recompensa, null)
                    val textViewRecompensa = popupView.findViewById<TextView>(R.id.textViewRecompensa)

                    textViewRecompensa.text="No puedes atacar con una mascota muerta crack"
                    popupWindow.width = LinearLayout.LayoutParams.WRAP_CONTENT
                    popupWindow.height = LinearLayout.LayoutParams.WRAP_CONTENT

                    popupWindow.isOutsideTouchable = true

                    popupWindow.contentView = popupView

                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
                    Handler().postDelayed({
                        popupWindow.dismiss()
                    }, 2500)

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