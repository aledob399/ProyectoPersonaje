package com.example.proyectopersonaje


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Looper

import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import java.util.logging.Handler


class Aventura : AppCompatActivity() {
    @SuppressLint("ResourceType", "WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aventura)
        val btn=findViewById<ImageButton>(R.id.btn)
        val dado=Dado()
        var num2=0
        var num1=0
        val strings = mutableListOf<String>()
        strings.add("R.drawable.uno")
        strings.add("R.drawable.dos")
        strings.add("R.drawable.tres")
        strings.add("R.drawable.cuatro")
        strings.add("R.drawable.cinco")
        strings.add("R.drawable.seis")
        btn.setOnClickListener {
            num1=0
            num2=0
            when (dado.tiradaUnica()) {

                1 -> {
                    repeat(20) { index ->
                        android.os.Handler(Looper.getMainLooper()).postDelayed({
                            val randomIndex = (2..6).random() // Generar un índice aleatorio para seleccionar una imagen diferente
                            val imageResource = when (randomIndex) {
                                2 -> R.drawable.dos
                                3 -> R.drawable.tres
                                4 -> R.drawable.cuatro
                                5 -> R.drawable.cinco
                                6 -> R.drawable.seis
                                else -> R.drawable.uno
                            }
                            btn.setImageResource(imageResource)
                        }, (index + 1) * 5000L)
                    }

                    btn.setImageResource(R.drawable.uno)
                    val intent = Intent(this,Objeto::class.java)
                    startActivity(intent)
                }
                2 -> {

                    btn.setImageResource(R.drawable.dos)
                    val intent = Intent(this,Ciudad::class.java)
                    startActivity(intent)
                }
                3 -> {

                    btn.setImageResource(R.drawable.tres)
                    val intent = Intent(this,Enemigo::class.java)
                    startActivity(intent)
                }
                4 -> {

                    btn.setImageResource(R.drawable.cuatro)
                    val intent = Intent(this,Mercader::class.java)
                    startActivity(intent)
                }
                5 -> {

                    btn.setImageResource(R.drawable.cinco)
                    val intent = Intent(this,Objeto::class.java)
                    startActivity(intent)
                }
                6 -> {

                    btn.setImageResource(R.drawable.seis)
                    val intent = Intent(this,Objeto::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}