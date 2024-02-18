package com.example.proyectopersonaje

import SolicitarTarea
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView // Importación de NestedScrollView
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2.*
import com.google.common.collect.Lists
import java.util.*

const val USUARIO = 0
const val BOT = 1
const val ENTRADA_DE_VOZ = 2

class DialogFlow : AppCompatActivity() {

    private var cliente: SessionsClient? = null
    private var sesion: SessionName? = null
    private val uuid: String = UUID.randomUUID().toString()
    private var asistenteVoz: TextToSpeech? = null
    private lateinit var cajaMensajes: EditText
    private lateinit var enviar: ImageButton
    private lateinit var microfono: ImageButton
    private lateinit var scrollChat: NestedScrollView // Declaración de NestedScrollView
    private lateinit var linearChat: LinearLayout
    private lateinit var cajaDeTexto: EditText

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_flow)

        scrollChat = findViewById(R.id.scroll_chat)
        linearChat = findViewById(R.id.linear_chat)
        cajaDeTexto = findViewById(R.id.cajaDeTexto) // Cambio realizado aquí

        val scrollview = findViewById<NestedScrollView>(R.id.scroll_chat)
        scrollview.post {
            scrollview.fullScroll(NestedScrollView.FOCUS_DOWN)
        }

        cajaDeTexto.setOnKeyListener { view, keyCode, event ->
            if (event.action === KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                        enviarMensaje(enviar)
                        true
                    }
                    else -> {
                    }
                }
            }
            false
        }

        enviar = findViewById(R.id.enviar)
        enviar.setOnClickListener(this::enviarMensaje)

        microfono = findViewById(R.id.microfono)
        microfono.setOnClickListener(this::enviarMensajeMicrofono)

        iniciarAsistente()
        iniciarAsistenteVoz()
    }

    private fun iniciarAsistente() {
        try {
            val config = resources.openRawResource(R.raw.credenciales)
            val credenciales = GoogleCredentials.fromStream(config)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"))
            val projectId = (credenciales as ServiceAccountCredentials).projectId
            val generarConfiguracion = SessionsSettings.newBuilder()
            val configurarSesiones =
                generarConfiguracion.setCredentialsProvider(FixedCredentialsProvider.create(credenciales))
                    .build()
            cliente = SessionsClient.create(configurarSesiones)
            sesion = SessionName.of(projectId, uuid)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun iniciarAsistenteVoz() {
        asistenteVoz = TextToSpeech(applicationContext) { status ->
            if (status != TextToSpeech.ERROR) {
                asistenteVoz?.language = Locale("es")
            }
        }
    }

    private fun enviarMensaje(view: View) {
        val mensaje = cajaDeTexto.text.toString()
        if (mensaje.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, getString(R.string.placeholder), Toast.LENGTH_LONG).show()
        } else {
            agregarTexto(mensaje, USUARIO)
            cajaDeTexto.setText("")
            val ingresarConsulta =
                QueryInput.newBuilder().setText(TextInput.newBuilder().setText(mensaje).setLanguageCode("es")).build()
            SolicitarTarea(this, sesion!!, cliente!!, ingresarConsulta).execute()
        }
    }

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

    private fun agregarTexto(mensaje: String, type: Int) {
        val layoutFrm: FrameLayout
        when (type) {
            USUARIO -> layoutFrm = agregarTextoUsuario()
            BOT -> layoutFrm = agregarTextoBot()
            else -> layoutFrm = agregarTextoBot()
        }
        layoutFrm.isFocusableInTouchMode = true
        linearChat.addView(layoutFrm)
        val textview = layoutFrm.findViewById<TextView>(R.id.msg_chat)
        textview.text = mensaje
        configTeclado.ocultarTeclado(this)
        layoutFrm.requestFocus()
        cajaDeTexto.requestFocus()
        if (type != USUARIO) asistenteVoz?.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null)
    }

    fun agregarTextoUsuario(): FrameLayout {
        val inflater = LayoutInflater.from(this)
        return inflater.inflate(R.layout.mensaje_usuario, null) as FrameLayout
    }

    fun agregarTextoBot(): FrameLayout {
        val inflater = LayoutInflater.from(this)
        return inflater.inflate(R.layout.mensaje_bot, null) as FrameLayout
    }

    fun validar(response: DetectIntentResponse?) {
        try {
            if (response != null) {
                var respuestaBot: String = ""
                if (response.queryResult.fulfillmentText == " ")
                    respuestaBot = response.queryResult.fulfillmentMessagesList[0].text.textList[0].toString()
                else
                    respuestaBot = response.queryResult.fulfillmentText
                agregarTexto(respuestaBot, BOT)
            } else {
                agregarTexto(getString(R.string.audio_no_se_entiende), BOT)
            }
        } catch (e: Exception) {
            agregarTexto(getString(R.string.ingresa_mensaje), BOT)
        }
    }

    override fun onActivityResult(codigoSolicitud: Int, codigoResultado: Int, datos: Intent?) {
        super.onActivityResult(codigoSolicitud, codigoResultado, datos)
        when (codigoSolicitud) {
            ENTRADA_DE_VOZ -> {
                if (codigoResultado == RESULT_OK
                    && datos != null
                ) {
                    val resultado = datos.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    cajaDeTexto.text = Editable.Factory.getInstance().newEditable(resultado?.get(0))
                    enviarMensaje(microfono)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (asistenteVoz != null) {
            asistenteVoz?.stop()
            asistenteVoz?.shutdown()
        }
    }
}
