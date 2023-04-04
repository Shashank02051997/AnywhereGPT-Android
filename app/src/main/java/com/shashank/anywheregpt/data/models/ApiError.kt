package com.shashank.anywheregpt.data.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ApiError(
    @SerializedName("error")
    val error: Error
) {
    @Keep
    data class Error(
        @SerializedName("message")
        val message: String,
        @SerializedName("type")
        val type: String
    )
}