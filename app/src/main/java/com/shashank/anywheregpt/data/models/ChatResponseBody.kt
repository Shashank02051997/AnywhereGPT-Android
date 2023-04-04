package com.shashank.anywheregpt.data.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ChatResponseBody(
    @SerializedName("choices")
    val choices: List<Choice>,
    @SerializedName("created")
    val created: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("model")
    val model: String,
    @SerializedName("object")
    val objectX: String,
    @SerializedName("usage")
    val usage: Usage
) {
    @Keep
    data class Choice(
        @SerializedName("finish_reason")
        val finishReason: String,
        @SerializedName("index")
        val index: Int,
        @SerializedName("message")
        val message: Message
    ) {
        @Keep
        data class Message(
            @SerializedName("content")
            val content: String,
            @SerializedName("role")
            val role: String
        )
    }

    @Keep
    data class Usage(
        @SerializedName("completion_tokens")
        val completionTokens: Int,
        @SerializedName("prompt_tokens")
        val promptTokens: Int,
        @SerializedName("total_tokens")
        val totalTokens: Int
    )
}