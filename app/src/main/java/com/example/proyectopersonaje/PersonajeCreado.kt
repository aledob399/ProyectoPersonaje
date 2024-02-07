package com.example.proyectopersonaje


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class PersonajeCreado : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personaje_creado)
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val btnEmpezar = findViewById<Button>(R.id.btnEmpezar)
        val p = intent.getParcelableExtra<Personaje>("personaje")
        var datos : TextView =findViewById(R.id.datos)
        var img : ImageView =findViewById(R.id.img)

        datos.text=p.toString()


        val idImagen = intent.getIntExtra("img", 0)
        img.setImageResource(idImagen);
        btnVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }
        btnEmpezar.setOnClickListener {
            val intent = Intent(this,Aventura::class.java)
            intent.putExtra("personaje",p)

            startActivity(intent)

        }

    }
}