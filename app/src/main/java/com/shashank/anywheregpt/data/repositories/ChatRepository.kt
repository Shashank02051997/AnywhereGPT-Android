package com.shashank.anywheregpt.data.repositories

import com.shashank.anywheregpt.data.models.ChatPostBody
import com.shashank.anywheregpt.data.models.ChatResponseBody
import com.shashank.anywheregpt.data.network.ApiInterface
import com.shashank.anywheregpt.data.network.SafeApiRequest
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val api: ApiInterface
) : SafeApiRequest() {

    suspend fun sendMessage(
        chatPostBody: ChatPostBody
    ): ChatResponseBody = apiRequest {
        api.sendMessage(chatPostBody)
    }
}