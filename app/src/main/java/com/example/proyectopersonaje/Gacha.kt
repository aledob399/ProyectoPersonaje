package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.os.postDelayed
import com.bumptech.glide.Glide
import kotlin.math.cos
import kotlin.random.Random

class Gacha : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var animation: RotateAnimation
    private var duration = 5000L // Duración total de la animación
    private var startTime: Long = 0
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gacha)


        var flag=true

        imageView = findViewById(R.id.ruletaImageView)
        val recompensaGif = findViewById<ImageView>(R.id.recompensaGif)
        animation = AnimationUtils.loadAnimation(this, R.anim.rotar) as RotateAnimation
        val personaje = intent.getParcelableExtra<Personaje>("personaje")
        val mascotas = intent.getParcelableArrayListExtra<Mascota>("mascotas")
        val btnContinuar = findViewById<Button>(R.id.btnContinuar)
        val btnTirar = findViewById<Button>(R.id.btnTirar)
        val recompensaImageView = findViewById<ImageView>(R.id.recompensa)
        val popupView = layoutInflater.inflate(R.layout.popup_recompensa, null)
        val popupWindow = PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true)
        val textViewRecompensa = popupView.findViewById<TextView>(R.id.textViewRecompensa)
        personaje!!.getMochila()!!.addArticulo(Articulo(Articulo.TipoArticulo.ORO,Articulo.Nombre.MONEDA,1,15,R.drawable.ciudad,1,Articulo.Rareza.RARO))
        personaje!!.getMochila()!!.addArticulo(Articulo(Articulo.TipoArticulo.ORO,Articulo.Nombre.MONEDA,1,15,R.drawable.ciudad,1,Articulo.Rareza.RARO))
        btnContinuar.setOnClickListener {
            val intent = Intent(this, Aventura::class.java)
            intent.putExtra("personaje", personaje)
            intent.putParcelableArrayListExtra("mascotas", mascotas)
            startActivity(intent)
        }
        val costeTirada = 15
        btnTirar.setOnClickListener {

            if(personaje!!.misMonedas()>=15){
                personaje.restarMonedas(costeTirada)
                val num = (1..6).random()
                val nombres = ArrayList<String>()
                nombres.add("Juan")
                nombres.add("Marcos")
                nombres.add("Julian")
                nombres.add("Fran")
                nombres.add("Eisier")
                startRotation()
                Handler().postDelayed({
                    stopRotation()
                    imageView.visibility=View.GONE
                    recompensaGif.visibility = View.VISIBLE
                    Glide.with(this)
                        .asGif()
                        .load(R.raw.gifrecompensa)
                        .into(recompensaGif)

                    recompensaGif.postDelayed({
                        recompensaGif.visibility = View.GONE
                    }, 2500)
                    imageView.visibility=View.VISIBLE
                }, 5000)
                Handler().postDelayed({
                    when (num) {
                        1 -> {
                            var recompensa: Mascota? = null
                            recompensa = Mascota(
                                nombres[(0..4).random()],
                                Mascota.tipoMascota.entries[(0..4).random()]

                            )
                            mascotas!!.add(recompensa)
                            textViewRecompensa.text = "¡Enhorabuena! Has recibido una mascota $recompensa"
                            when(recompensa.getAtributo()){
                                Mascota.tipoMascota.AGUA->recompensaImageView.setImageResource(R.drawable.mascota_agua)
                                Mascota.tipoMascota.LUZ->recompensaImageView.setImageResource(R.drawable.mascota_luz)
                                Mascota.tipoMascota.PLANTA->recompensaImageView.setImageResource(R.drawable.mascota_agua)
                                Mascota.tipoMascota.OSCURIDAD->recompensaImageView.setImageResource(R.drawable.mascota_oscuridad)
                                Mascota.tipoMascota.FUEGO->recompensaImageView.setImageResource(R.drawable.mascota_fuego)
                                else -> {recompensaImageView.setImageResource(R.drawable.mascota_agua)}
                            }

                        }

                        2 -> {
                            var recompensa: Articulo? = null
                            recompensa = Articulo(
                                Articulo.TipoArticulo.PROTECCION,
                                Articulo.Nombre.ESCUDO,
                                (1..7).random(),
                                (1..50).random(),
                                R.drawable.objetocuatro,
                                1,Articulo.Rareza.entries[(0..3).random()]
                            )
                            personaje.getMochila()!!.addArticulo(recompensa)
                            textViewRecompensa.text = "¡Enhorabuena! Has recibido un articulo $recompensa"
                            recompensaImageView.setImageResource(recompensa.getUrl())
                        }

                        3 -> {
                            var recompensa: Articulo? = null
                            recompensa = Articulo(
                                Articulo.TipoArticulo.ARMA,
                                Articulo.Nombre.ESPADA,
                                (1..7).random(),
                                (1..50).random(),
                                R.drawable.objetodos,
                                1,Articulo.Rareza.entries[(0..3).random()]
                            )
                            personaje.getMochila()!!.addArticulo(recompensa)
                            textViewRecompensa.text = "¡Enhorabuena! Has recibido un articulo $recompensa"
                            recompensaImageView.setImageResource(recompensa.getUrl())
                        }

                        4 -> {
                            var recompensa: Articulo? = null
                            recompensa = Articulo(
                                Articulo.TipoArticulo.ARMA,
                                Articulo.Nombre.DAGA,
                                (1..7).random(),
                                (1..50).random(),
                                R.drawable.objetoocho,
                                1,Articulo.Rareza.entries[(0..3).random()]
                            )
                            personaje.getMochila()!!.addArticulo(recompensa)
                            textViewRecompensa.text = "¡Enhorabuena! Has recibido un articulo $recompensa"
                            recompensaImageView.setImageResource(recompensa.getUrl())
                        }

                        5 -> {

                            textViewRecompensa.text = "Mala suerte,no ha salido nada"
                            recompensaImageView.setImageResource(R.drawable.nada)
                        }

                        6 -> {
                            var recompensa: Mascota? = null
                            recompensa = Mascota(
                                nombres[(0..4).random()],
                                Mascota.tipoMascota.entries[(0..4).random()]
                            )
                            mascotas!!.add(recompensa)
                            textViewRecompensa.text = "¡Enhorabuena! Has recibido una mascota $recompensa"
                            when(recompensa.getAtributo()){
                                Mascota.tipoMascota.AGUA->recompensaImageView.setImageResource(R.drawable.mascota_agua)
                                Mascota.tipoMascota.LUZ->recompensaImageView.setImageResource(R.drawable.mascota_luz)
                                Mascota.tipoMascota.PLANTA->recompensaImageView.setImageResource(R.drawable.mascota_agua)
                                Mascota.tipoMascota.OSCURIDAD->recompensaImageView.setImageResource(R.drawable.mascota_oscuridad)
                                Mascota.tipoMascota.FUEGO->recompensaImageView.setImageResource(R.drawable.mascota_fuego)
                                else -> {recompensaImageView.setImageResource(R.drawable.mascota_agua)}
                            }
                        }


                    }
                    popupWindow.width = LinearLayout.LayoutParams.WRAP_CONTENT
                    popupWindow.height = LinearLayout.LayoutParams.WRAP_CONTENT

                    popupWindow.isOutsideTouchable = true

                    popupWindow.contentView = popupView

                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
                    recompensaImageView.visibility=View.VISIBLE
                    Handler().postDelayed({
                        recompensaImageView.visibility=View.GONE
                        popupWindow.dismiss()
                    },3000)
                },7600)






            }else{
                textViewRecompensa.text = "Necesitas $costeTirada y solo tienes ${personaje.misMonedas()}"
                popupWindow.width = LinearLayout.LayoutParams.WRAP_CONTENT
                popupWindow.height = LinearLayout.LayoutParams.WRAP_CONTENT

                popupWindow.isOutsideTouchable = true

                popupWindow.contentView = popupView

                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
                Handler().postDelayed({
                    popupWindow.dismiss()
                }, 3000)
            }



        }





        }


    private fun startRotation() {
        startTime = System.currentTimeMillis()

        // Iniciar la rotación con un interpolador lineal
        animation.interpolator = LinearInterpolator()
        animation.duration = duration
        imageView.startAnimation(animation)

        // Actualizar la duración de la animación con un retraso de 100ms
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                val elapsedTime = System.currentTimeMillis() - startTime
                val remainingTime = duration - elapsedTime
                val progress = elapsedTime.toFloat() / duration

                // Disminuir la duración de la animación gradualmente
                if (remainingTime > 0) {
                    animation.duration = calculateNewDuration(progress)
                    handler.postDelayed(this, 100)
                }
            }
        }, 100)
    }
    private fun stopRotation() {
        // Calcula el tiempo transcurrido desde que se inició la rotación
        val elapsedTime = System.currentTimeMillis() - startTime

        // Calcula la nueva duración de la animación de forma gradual
        val newDuration = (duration - elapsedTime).coerceAtLeast(0)

        // Actualiza la duración de la animación
        animation.duration = newDuration

        // Detiene la animación
        imageView.clearAnimation()
    }


    private fun calculateNewDuration(progress: Float): Long {
        // Aplica una fórmula para ajustar la duración gradualmente
        return (duration * (1 - progress)).toLong()
    }



}