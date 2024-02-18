package com.example.proyectopersonaje

import com.google.cloud.dialogflow.v2.DetectIntentResponse


interface BotReply {
    fun callback(returnResponse: DetectIntentResponse?)
}