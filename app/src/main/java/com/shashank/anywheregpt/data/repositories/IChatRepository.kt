package com.shashank.anywheregpt.data.repositories

import com.shashank.anywheregpt.data.models.ChatPostBody
import com.shashank.anywheregpt.data.models.ChatResponseBody

interface IChatRepository {
    suspend fun sendMessage(
        chatPostBody: ChatPostBody
    ): ChatResponseBody
}