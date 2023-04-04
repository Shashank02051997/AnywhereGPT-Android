package com.shashank.anywheregpt.data.interceptors

import android.content.Context
import com.shashank.anywheregpt.utils.SharedPref
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val apiKey = SharedPref.getStringPref(context, SharedPref.KEY_API_KEY)
        if (apiKey.isNotBlank()) {
            Timber.i("Token = Bearer $apiKey")
            requestBuilder.addHeader("Authorization", "Bearer $apiKey")
        }
        return chain.proceed(requestBuilder.build())
    }
}
