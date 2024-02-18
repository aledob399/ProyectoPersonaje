import android.app.Activity
import android.os.AsyncTask
import com.example.proyectopersonaje.DialogFlow
import com.google.cloud.dialogflow.v2.DetectIntentRequest
import com.google.cloud.dialogflow.v2.DetectIntentResponse
import com.google.cloud.dialogflow.v2.QueryInput
import com.google.cloud.dialogflow.v2.SessionName
import com.google.cloud.dialogflow.v2.SessionsClient

class SolicitarTarea : AsyncTask<Void, Void, DetectIntentResponse> {

    var actividad: Activity? = null
    private var sesion: SessionName? = null
    private var sesionesCliente: SessionsClient? = null
    private var entradaConsulta: QueryInput? = null

    constructor(
        actividad: Activity,
        sesion: SessionName,
        sesionesCliente: SessionsClient,
        entradaConsulta: com.google.cloud.dialogflow.v2.QueryInput
    ) {
        this.actividad = actividad
        this.sesion = sesion
        this.sesionesCliente = sesionesCliente
        this.entradaConsulta = entradaConsulta
    }

    override fun doInBackground(vararg params: Void?): DetectIntentResponse? {
        try {
            val detectarIntentosolicitarTarea = DetectIntentRequest.newBuilder()
                .setSession(sesion.toString())
                .setQueryInput(entradaConsulta)
                .build()
            return sesionesCliente?.detectIntent(detectarIntentosolicitarTarea)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(result: DetectIntentResponse?) {
        (actividad as DialogFlow).validar(result)
    }
}