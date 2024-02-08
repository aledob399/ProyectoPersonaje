package com.example.proyectopersonaje

import android.content.Context
import com.google.gson.Gson

class SaveManager(private val context: Context) {

    fun savePersonaje(personaje: Personaje) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val personajeJson = gson.toJson(personaje)
        editor.putString("personaje", personajeJson)
        editor.apply()
    }

    fun loadPersonaje(): Personaje? {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val personajeJson = sharedPreferences.getString("personaje", null)
        return if (personajeJson != null) {
            gson.fromJson(personajeJson, Personaje::class.java)
        } else {
            null
        }
    }
}
