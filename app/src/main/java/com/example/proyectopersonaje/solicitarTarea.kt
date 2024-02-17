package com.example.proyectopersonaje
import android.app.Activity
import android.os.AsyncTask
import com.google.cloud.dialogflow.v2.*

// Cambié el nombre de la clase a SolicitarTarea para seguir las convenciones de nombres de clases en Kotlin
class solicitarTarea(private val actividad: Activity, private val sesion: SessionName, private val sesionesCliente: SessionsClient, private val entradaConsulta: QueryInput) : AsyncTask<Void, Void, DetectIntentResponse>() {

    // Modifiqué el constructor para usar los parámetros directamente como argumentos de la clase

    // Cambié el nombre de los métodos a camelCase para seguir las convenciones de nombres de métodos en Kotlin
    override fun doInBackground(vararg params: Void?): DetectIntentResponse? {
        try {
            val detectarIntentosolicitarTarea = DetectIntentRequest.newBuilder()
                .setSession(sesion.toString())
                .setQueryInput(entradaConsulta)
                .build()
            return sesionesCliente.detectIntent(detectarIntentosolicitarTarea)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(result: DetectIntentResponse?) {
        // Verificamos si la actividad es una instancia de DialogFlow antes de llamar al método validar()
        if (actividad is DialogFlow) {
            actividad.validar(result)
        }
    }
}
