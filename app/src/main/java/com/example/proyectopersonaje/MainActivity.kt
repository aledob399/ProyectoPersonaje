package com.example.proyectopersonaje



import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val signUpButton=findViewById<Button>(R.id.acceder)
        val analytics: FirebaseAnalytics =FirebaseAnalytics.getInstance(this)
        val bundle=Bundle()
        bundle.putString("mensaje","Integracion de firebase completa")
        analytics.logEvent("InitScreep",bundle)
        setup()
    }
    private fun setup(){
        val signUpButton=findViewById<Button>(R.id.registrarse)
        val logInButton=findViewById<Button>(R.id.acceder)
        val emailEditText=findViewById<EditText>(R.id.email)
        val passwordEditText=findViewById<EditText>(R.id.contraseña)
        title="Autentificacion"
        signUpButton.setOnClickListener{
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEditText.text.toString(),passwordEditText.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        val intent=Intent(this,CreacionPersonaje::class.java)
                        startActivity(intent)
                    }else{
                        showAlert()
                    }
                }
            }
        }
        logInButton.setOnClickListener{
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.text.toString(),passwordEditText.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        val intent=Intent(this,CreacionPersonaje::class.java)
                        startActivity(intent)
                        //showHome(it.result?.user?.email?:"",ProviderType.BASIC)
                    }else{
                        showAlert()
                    }
                }
            }
        }
    }
    private fun showAlert(){
        val builder=AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autentificando el usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog=builder.create()
        dialog.show()
    }

}