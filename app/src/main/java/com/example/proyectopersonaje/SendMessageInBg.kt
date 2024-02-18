package com.example.proyectopersonaje

import android.os.AsyncTask
import android.util.Log
import com.google.cloud.dialogflow.v2.DetectIntentRequest
import com.google.cloud.dialogflow.v2.DetectIntentResponse
import com.google.cloud.dialogflow.v2.QueryInput
import com.google.cloud.dialogflow.v2.SessionName
import com.google.cloud.dialogflow.v2.SessionsClient


 class SendMessageInBg(
     botReply: BotReply, session: SessionName, sessionsClient: SessionsClient,
     queryInput: QueryInput
) : AsyncTask<Void?, Void?, DetectIntentResponse?>() {
    private val session: SessionName
    private val sessionsClient: SessionsClient
    private val queryInput: QueryInput
    private val TAG = "async"
    private val botReply: BotReply

    init {
        this.botReply = botReply
        this.session = session
        this.sessionsClient = sessionsClient
        this.queryInput = queryInput
    }

     override fun doInBackground(vararg params: Void?): DetectIntentResponse? {
        try {
            val detectIntentRequest = DetectIntentRequest.newBuilder()
                .setSession(session.toString())
                .setQueryInput(queryInput)
                .build()
            return sessionsClient.detectIntent(detectIntentRequest)
        } catch (e: Exception) {
            Log.d(TAG, "doInBackground: " + e.message)
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(response: DetectIntentResponse?) {
        botReply.callback(response)
    }
}