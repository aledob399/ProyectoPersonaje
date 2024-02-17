package com.example.proyectopersonaje

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.cloud.dialogflow.v2.DetectIntentResponse
import com.google.cloud.dialogflow.v2.QueryInput
import com.google.cloud.dialogflow.v2.SessionName
import com.google.cloud.dialogflow.v2.SessionsClient
import com.google.cloud.dialogflow.v2.TextInput
//import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

const val USUARIO = 0
const val BOT = 1
const val ENTRADA_DE_VOZ = 2

class DialogFlow : AppCompatActivity() {

    // Variables
    private var cliente: SessionsClient? = null
    private var sesion: SessionName? = null
    private val uuid: String = UUID.randomUUID().toString()
    private var asistente_voz: TextToSpeech? = null

    private lateinit var cajaMensajes: EditText
    private lateinit var enviar: ImageButton
    private lateinit var microfono: ImageButton
    private lateinit var scroll_chat: ScrollView
    private lateinit var linear_chat: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        scroll_chat = findViewById(R.id.scroll_chat)
        linear_chat = findViewById(R.id.linear_chat)
        cajaMensajes = findViewById(R.id.cajadetexto)
        enviar = findViewById(R.id.enviar)
        microfono = findViewById(R.id.microfono)

        scroll_chat.post {
            scroll_chat.fullScroll(ScrollView.FOCUS_DOWN)
        }

        cajaMensajes.setOnKeyListener { view, keyCode, event ->
            if (event.action === KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                        enviarMensaje(enviar)
                        true
                    }
                    else -> false
                }
            } else false
        }

        // Set OnClickListener para el botón de enviar mensaje
        enviar.setOnClickListener {
            enviarMensaje(it)
        }

        // Set OnClickListener para el botón de micrófono
        microfono.setOnClickListener {
            enviarMensajeMicrofono(it)
        }

        // Llamar al método para iniciar el asistente de voz
        iniciarAsistenteVoz()
    }

    // Función para iniciar el asistente de voz
    private fun iniciarAsistenteVoz() {
        asistente_voz = TextToSpeech(applicationContext) { status ->
            if (status != TextToSpeech.ERROR) {
                asistente_voz?.language = Locale("es")
            }
        }
    }

    // Función para enviar un mensaje
    private fun enviarMensaje(view: View) {
        val mensaje = cajaMensajes.text.toString().trim()

        if (mensaje.isEmpty()) {
            Toast.makeText(this@DialogFlow, getString(R.string.placeholder), Toast.LENGTH_LONG).show()
        } else {
            agregarTexto(mensaje, USUARIO)
            cajaMensajes.setText("")

            // Enviamos la consulta del usuario al Bot
            val ingresarConsulta = QueryInput.newBuilder()
                .setText(TextInput.newBuilder().setText(mensaje).setLanguageCode("es")).build()
            solicitarTarea(this@DialogFlow, sesion!!, cliente!!, ingresarConsulta).execute()
        }
    }

    // Función para enviar un mensaje por medio del micrófono
    private fun enviarMensajeMicrofono(view: View) {
        val intento = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intento.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intento.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intento.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.mensajedevoz))

        try {
            startActivityForResult(intento, ENTRADA_DE_VOZ)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(applicationContext, getString(R.string.mensajedevoznoadmitido), Toast.LENGTH_SHORT).show()
        }
    }

    // Función para agregar un texto al chat
    private fun agregarTexto(mensaje: String, type: Int) {
        val layoutFrm: FrameLayout
        val inflater = LayoutInflater.from(this@DialogFlow)

        // Seleccionar el layout dependiendo del tipo de mensaje
        layoutFrm = if (type == USUARIO) {
            inflater.inflate(R.layout.mensaje_usuario, null) as FrameLayout
        } else {
            inflater.inflate(R.layout.mensaje_bot, null) as FrameLayout
        }

        layoutFrm.isFocusableInTouchMode = true
        linear_chat.addView(layoutFrm)

        val textView = layoutFrm.findViewById<TextView>(R.id.msg_chat)
        textView.text = mensaje

        // Ocultar el teclado del dispositivo
        configTeclado.ocultarTeclado(this)

        // Reproducir el mensaje en caso de que sea un mensaje del bot
        if (type != USUARIO) {
            asistente_voz?.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null)
        }
    }
    fun validar(result: DetectIntentResponse?) {
        // Verificar si la respuesta no es nula
        if (result != null) {
            // Obtener la respuesta del agente de Dialogflow
            val fulfillmentText = result.queryResult.fulfillmentText

            // Manejar la respuesta como desees, por ejemplo, mostrarla en la interfaz de usuario
            agregarTexto(fulfillmentText, BOT)
        } else {
            // Manejar el caso en que no se reciba una respuesta adecuada
            agregarTexto("No se recibió una respuesta adecuada de Dialogflow.", BOT)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if (asistente_voz != null) {
            asistente_voz?.stop()
            asistente_voz?.shutdown()
        }
    }
}
