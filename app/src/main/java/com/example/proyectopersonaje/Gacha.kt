package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class Gacha : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gacha)
        val personaje=intent.getParcelableExtra<Personaje>("personaje")
        val mascotas = intent.getParcelableArrayListExtra<Mascota>("mascotas")
        val btnContinuar=findViewById<Button>(R.id.continuar)
        val btnTirar=findViewById<Button>(R.id.Tirar)
        btnContinuar.setOnClickListener {
            val intent = Intent(this,Aventura::class.java)
            intent.putExtra("personaje",personaje)
            intent.putParcelableArrayListExtra("mascotas", mascotas)
            startActivity(intent)
        }
        btnTirar.setOnClickListener {
            val num=(1..6).random()
            val nombres=ArrayList<String>()
            nombres.add("Juan")
            nombres.add("Marcos")
            nombres.add("Julian")
            nombres.add("Fran")
            nombres.add("Eisier")

            when(num){
                1->{
                    var recompensa: Mascota? =null
                    recompensa =  Mascota(nombres[(0..4).random()], Mascota.tipoMascota.entries[(0..4).random()])
                }
                2->{
                    var recompensa: Articulo? =null
                    recompensa =  Articulo(Articulo.TipoArticulo.ORO,Articulo.Nombre.MONEDA,0,15,R.drawable.moneda,1)
                }
                3->{
                    var recompensa: Articulo? =null
                    recompensa =  Articulo(Articulo.TipoArticulo.ARMA,Articulo.Nombre.ESPADA,2,23,R.drawable.moneda,1)
                }
                4->{
                    var recompensa: Articulo? =null
                    recompensa =  Articulo(Articulo.TipoArticulo.ARMA,Articulo.Nombre.DAGA,2,23,R.drawable.moneda,1)
                }
                5->{
                    var recompensa: String? ="Nada"

                }
                6->{
                    var recompensa: Mascota? =null
                    recompensa =  Mascota(nombres[(0..4).random()], Mascota.tipoMascota.entries[(0..4).random()])
                }



            }

        }


    }

}