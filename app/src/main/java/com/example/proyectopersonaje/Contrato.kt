package com.example.proyectopersonaje

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Contrato : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contrato)
        val personaje=intent.getParcelableExtra<Personaje>("personaje")
        val mascotas = intent.getParcelableArrayListExtra<Mascota>("mascotas")
        val btnContinuar=findViewById<Button>(R.id.continuar)
        val btnTirar=findViewById<Button>(R.id.Tirar)
    }
}