package com.example.proyectopersonaje

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2.DetectIntentResponse
import com.google.cloud.dialogflow.v2.QueryInput
import com.google.cloud.dialogflow.v2.SessionName
import com.google.cloud.dialogflow.v2.SessionsClient
import com.google.cloud.dialogflow.v2.SessionsSettings
import com.google.cloud.dialogflow.v2.TextInput
import com.google.common.collect.Lists
import java.util.UUID

class ChatbotActivity : AppCompatActivity(), BotReply {


    private var chatView: RecyclerView? = null
    private var chatAdapter: ChatAdapter? = null
    private var messageList: MutableList<Message> = ArrayList()
    private var editMessage: EditText? = null
    private var btnSend: ImageButton? = null
    private var sessionsClient: SessionsClient? = null
    private var sessionName: SessionName? = null
    private val uuid = UUID.randomUUID().toString()
    private val TAG = "ChatbotActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chatbot)

        val personaje=intent.getParcelableExtra<Personaje>("personaje")
        val creado=intent.getBooleanExtra("creado",false)

        var mascotas = intent.getParcelableArrayListExtra<Mascota>("mascotas")!!
        val salirBoton = findViewById<Button>(R.id.salirboton)

        salirBoton.setOnClickListener {
            val intent = Intent(this, Aventura::class.java)
            intent.putExtra("personaje",personaje)
            intent.putParcelableArrayListExtra("mascotas", mascotas)
            startActivity(intent)
            finish()
        }

        chatView = findViewById<RecyclerView>(R.id.chatView)
        editMessage = findViewById<EditText>(R.id.editMessage)
        btnSend = findViewById<ImageButton>(R.id.btnSend)
        chatAdapter = ChatAdapter(messageList, this)
        chatView?.adapter = chatAdapter

        btnSend?.setOnClickListener {
            val message = editMessage?.text.toString()
            if (message.isNotEmpty()) {
                messageList.add(Message(message, false))
                editMessage?.setText("")
                sendMessageToBot(message)
                chatAdapter?.notifyDataSetChanged()
                chatView?.layoutManager?.scrollToPosition(messageList.size - 1)
            } else {
                Toast.makeText(this@ChatbotActivity, "Please enter text!", Toast.LENGTH_SHORT).show()
            }
        }
        setUpBot()
    }

    private fun setUpBot() {
        try {
            val stream = resources.openRawResource(R.raw.credenciales)
            val credentials = GoogleCredentials.fromStream(stream)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"))
            val projectId = (credentials as ServiceAccountCredentials).projectId
            val settingsBuilder = SessionsSettings.newBuilder()
            val sessionsSettings = settingsBuilder.setCredentialsProvider(
                FixedCredentialsProvider.create(credentials)
            ).build()
            sessionsClient = SessionsClient.create(sessionsSettings)
            sessionName = SessionName.of(projectId, uuid)
            Log.d(TAG, "projectId : $projectId")
        } catch (e: Exception) {
            Log.d(TAG, "setUpBot: " + e.message)
        }
    }
    private fun sendMessageToBot(message: String) {
        val input = QueryInput.newBuilder()
            .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build()
        SendMessageInBg(this, sessionName!!, sessionsClient!!, input).execute()
    }
    override fun callback(returnResponse: DetectIntentResponse?) {
        if (returnResponse != null) {
            val botReply = returnResponse.queryResult.fulfillmentText
            if (botReply.isNotEmpty()) {
                messageList.add(Message(botReply, true))
                chatAdapter?.notifyDataSetChanged()
                chatView?.layoutManager?.scrollToPosition(messageList.size - 1)
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Failed to connect!", Toast.LENGTH_SHORT).show()
        }
    }

}
