package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random

class Mazmorras : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mazmorra)
        val personaje=intent.getParcelableExtra<Personaje>("personaje")
        val mascotas = intent.getParcelableArrayListExtra<Mascota>("mascotas")
        val btnEntrar=findViewById<Button>(R.id.entrar)
        val btnContinuar=findViewById<Button>(R.id.continuar)
        val textoMazmorra=findViewById<TextView>(R.id.textoMazmorra)
        val mazmorra=Mazmorra((1..3).random(), Mazmorra.TipoCondicion.entries[(0..2).random()])
        textoMazmorra.text=when(mazmorra.getCondicion()){
            Mazmorra.TipoCondicion.MENOSATAQUE->"Mazmorra con -20% de ataque de dificultad ${mazmorra.getDificultad()} con ${mazmorra.getEnemigos().size} enemigos"
            Mazmorra.TipoCondicion.MENOSVIDA->"Mazmorra con -20% de vida de dificultad ${mazmorra.getDificultad()} con ${mazmorra.getEnemigos().size} enemigos"
            else -> ""
        }
        btnEntrar.setOnClickListener {





        }
        btnContinuar.setOnClickListener {
            val intent=Intent(this,Aventura::class.java)
            intent.putExtra("personaje",personaje)
            intent.putParcelableArrayListExtra("mascotas", mascotas)
            startActivity(Intent())
        }
    }

}