package com.example.proyectopersonaje
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.proyectopersonaje.Mascota

class Mascotas : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mascotas) // Reemplaza "tu_layout" con el nombre real de tu archivo XML
        val btnContinuar=findViewById<Button>(R.id.continuar)
        val btnHuevos=findViewById<Button>(R.id.huevos)
        val personaje=intent.getParcelableExtra<Personaje>("personaje")

        val mascotas: ArrayList<Mascota>? = intent.getParcelableArrayListExtra("mascotas")
        val cardContainer: LinearLayout = findViewById(R.id.cardContainer)
        btnContinuar.setOnClickListener {
            val intent= Intent(this,Aventura::class.java)
            intent.putExtra("personaje",personaje)
            intent.putParcelableArrayListExtra("mascotas", mascotas)
            startActivity(intent)
        }
        // Verificar si el ArrayList no es nulo y no está vacío
        if (mascotas != null && mascotas.isNotEmpty()) {
            // Iterar sobre cada mascota en el ArrayList
            for (mascota in mascotas) {
                // Crear un nuevo CardView
                val cardView = CardView(this)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(20, 20, 20, 20)
                cardView.layoutParams = layoutParams
                cardView.cardElevation = 8f
                cardView.radius = 16f

                // Crear un nuevo LinearLayout dentro del CardView
                val linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                linearLayout.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                linearLayout.setPadding(20, 20, 20, 20)

                // Crear un ImageView para la imagen de la mascota (asumiendo que tienes la imagen en un recurso drawable)
                val imageView = ImageView(this)
                when(mascota.getAtributo()){
                    Mascota.tipoMascota.AGUA-> imageView.setImageResource(R.drawable.mascota_agua)
                    Mascota.tipoMascota.FUEGO-> imageView.setImageResource(R.drawable.mascota_fuego)
                    Mascota.tipoMascota.OSCURIDAD-> imageView.setImageResource(R.drawable.mascota_oscuridad)
                    Mascota.tipoMascota.LUZ-> imageView.setImageResource(R.drawable.mascota_luz)
                    else -> {imageView.setImageResource(R.drawable.mascota_luz)}
                }
                 // Reemplaza "nombre_imagen" con el nombre real de tu imagen
                val imageLayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                imageView.layoutParams = imageLayoutParams

                // Crear un TextView para mostrar la información de la mascota
                val textView = TextView(this)
                textView.text = mascota.toString() // Aquí debes ajustar cómo quieres mostrar la información de la mascota
                val textLayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                textView.layoutParams = textLayoutParams

                // Agregar la imagen y el TextView al LinearLayout
                linearLayout.addView(imageView)
                linearLayout.addView(textView)

                // Agregar el LinearLayout al CardView
                cardView.addView(linearLayout)

                // Agregar el CardView al contenedor en tu layout
                cardContainer.addView(cardView)
            }
        }
    }
}
