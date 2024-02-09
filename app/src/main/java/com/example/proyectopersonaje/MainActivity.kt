package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType {
    BASIC
}

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    // Declarar dbHelper como una variable miembro
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val signUpButton = findViewById<Button>(R.id.acceder)
        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("mensaje", "Integracion de firebase completa")
        analytics.logEvent("InitScreep", bundle)

        // Inicializar dbHelper


        setup()
    }

    private fun setup() {
        var db = DatabaseHelper(this)
        val signUpButton = findViewById<Button>(R.id.registrarse)
        val logInButton = findViewById<Button>(R.id.acceder)
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.contraseña)
        title = "Autentificacion"

        signUpButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, CreacionPersonaje::class.java)
                        startActivity(intent)
                    } else {
                        showAlert()
                    }
                }
            }
        }

        logInButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idUsuarioAuth = FirebaseAuth.getInstance().uid

                        // Obtener el personaje de la base de datos usando el ID de autenticación
                        val dbHelper = DatabaseHelper(this)
                        val personaje = dbHelper.obtenerPersonaje(idUsuarioAuth.toString())

                        // Verificar si el personaje es null antes de intentar acceder a sus propiedades
                        if (personaje != null) {
                            val intent = Intent(this, PersonajeCreado::class.java)
                            intent.putExtra("modoRegistro", false) // El usuario ya está registrado
                            intent.putExtra("personaje", personaje)
                            startActivity(intent)
                        } else {
                            // Mostrar mensaje o realizar alguna acción en caso de que el personaje no se encuentre en la base de datos
                            Toast.makeText(this,"Personaje no encontrado en la base de datos.",
                                Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        showAlert()
                    }
                }
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autentificando el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, CreacionPersonaje::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
}
