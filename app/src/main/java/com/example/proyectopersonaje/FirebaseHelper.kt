package com.example.proyectopersonaje
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseHelper {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val personajesRef: DatabaseReference = database.getReference("personajes")

    fun guardarPersonajeEnFirebase(personaje: Personaje, idPersonaje: String) {
        // Guardar el personaje en Firebase Realtime Database
        personajesRef.child(idPersonaje).setValue(personaje)
    }

    fun obtenerPersonajeDeFirebase(idPersonaje: String, callback: (Personaje?) -> Unit) {
        // Obtener el personaje de Firebase Realtime Database
        personajesRef.child(idPersonaje).get().addOnSuccessListener {
            val personaje = it.getValue(Personaje::class.java)
            callback(personaje)
        }.addOnFailureListener {
            callback(null)
        }
    }
}
