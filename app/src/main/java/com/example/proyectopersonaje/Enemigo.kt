package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Enemigo : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enemigo)
        val btnPelear=findViewById<Button>(R.id.pelear)
        val btnContinuar=findViewById<Button>(R.id.continuar)
        val personaje=intent.getParcelableExtra<Personaje>("personaje")
        btnPelear.setOnClickListener {
            val intent= Intent(this,Aventura::class.java)
            intent.putExtra("personaje",personaje)
            startActivity(intent)
        }
        btnContinuar.setOnClickListener {
            val intent= Intent(this,Aventura::class.java)
            intent.putExtra("personaje",personaje)
            startActivity(intent)
        }
    }
}