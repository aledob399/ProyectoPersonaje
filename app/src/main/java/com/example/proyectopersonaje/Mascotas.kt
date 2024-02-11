package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Mascotas : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mascotas)
        val personaje=intent.getParcelableExtra<Personaje>("personaje")
        val mascotas = intent.getParcelableArrayListExtra<Mascota>("mascotas")
        val btnMascota=findViewById<Button>(R.id.mascota)
        val btnContinuar=findViewById<Button>(R.id.continuar)
        val mas =Mascota("ha",Mascota.tipoMascota.AGUA)
        btnMascota.setOnClickListener {
            mascotas!!.add(mas)
        }
        btnContinuar.setOnClickListener {
            val intent= Intent(this,Aventura::class.java)
            intent.putExtra("personaje",personaje)
            intent.putParcelableArrayListExtra("mascotas", mascotas)
            startActivity(intent)
        }
    }
}