package com.example.proyectopersonaje

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Gacha : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gacha)

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