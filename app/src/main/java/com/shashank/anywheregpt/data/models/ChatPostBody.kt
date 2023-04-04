package com.shashank.anywheregpt.data.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ChatPostBody(
    @SerializedName("frequency_penalty")
    val frequencyPenalty: Double,
    @SerializedName("max_tokens")
    val maxTokens: Int,
    @SerializedName("messages")
    val messages: List<Message>,
    @SerializedName("model")
    val model: String,
    @SerializedName("presence_penalty")
    val presencePenalty: Double,
    @SerializedName("temperature")
    val temperature: Double,
    @SerializedName("top_p")
    val topP: Int
) {
    @Keep
    data class Message(
        @SerializedName("content")
        val content: String,
        @SerializedName("role")
        val role: String
    )
}