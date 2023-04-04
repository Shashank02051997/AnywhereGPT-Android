package com.shashank.anywheregpt.data.network

import com.shashank.anywheregpt.data.models.ChatPostBody
import com.shashank.anywheregpt.data.models.ChatResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    // <editor-fold desc="Post Requests">

    @POST("v1/chat/completions")
    suspend fun sendMessage(
        @Body chatPostBody: ChatPostBody
    ): Response<ChatResponseBody>


    // </editor-fold>


}