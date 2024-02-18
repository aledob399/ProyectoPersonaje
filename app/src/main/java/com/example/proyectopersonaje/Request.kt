package com.example.proyectopersonaje

import android.content.ContentValues
import android.os.AsyncTask
import android.util.Log
import com.google.cloud.dialogflow.v2.DetectIntentRequest
import com.google.cloud.dialogflow.v2.DetectIntentResponse
import com.google.cloud.dialogflow.v2.QueryInput
import com.google.cloud.dialogflow.v2.SessionName
import com.google.cloud.dialogflow.v2.SessionsClient


abstract class Request(
    private val mInterface: BotReply,
    private val session: SessionName,
    private val sessionsClient: SessionsClient,
    private val queryInput: QueryInput
) : AsyncTask<Void?, Void?, DetectIntentResponse?>() {
    protected override fun doInBackground(vararg params: Void?): DetectIntentResponse? {
        try {
            val detectIntentRequest = DetectIntentRequest.newBuilder()
                .setSession(session.toString())
                .setQueryInput(queryInput)
                .build()
            return sessionsClient.detectIntent(detectIntentRequest)
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "doInBackground: " + e.message)
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(response: DetectIntentResponse?) {
        mInterface.callback(response)
    }
}