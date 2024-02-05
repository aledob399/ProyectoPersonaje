package com.example.proyectopersonaje


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Ciudad : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ciudad)
        val btnEntrar=findViewById<Button>(R.id.entrar)
        val btnContinuar=findViewById<Button>(R.id.continuar)
        btnEntrar.setOnClickListener {
            val intent= Intent(this,Aventura::class.java)
            startActivity(intent)
        }
        btnContinuar.setOnClickListener {
            val intent= Intent(this,Aventura::class.java)
            startActivity(intent)
        }

    }
}