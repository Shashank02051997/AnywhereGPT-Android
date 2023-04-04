package com.shashank.anywheregpt.utils

import android.content.Context
import com.google.gson.Gson

object SharedPref {

    private const val ANYWHERE_GPT_PREFERENCE = "AnywhereGPTPreference"
    // <editor-fold desc="Preference Keys">

    const val KEY_API_KEY = "api_key"
    const val KEY_TOKEN_LENGTH = "token_length"

    // </editor-fold>

    // <editor-fold desc="Default Values">
    private const val NO_VALUE_PREF = ""
    private const val NO_VALUE = 0
    private const val NO_VALUE_LONG = 0L
    // </editor-fold>

    // <editor-fold desc="Get/Set/Clear Pref">

    fun setStringPref(context: Context, key: String, value: String) {
        context.getSharedPreferences(ANYWHERE_GPT_PREFERENCE, Context.MODE_PRIVATE).edit()
            .putString(key, value)
            .apply()
    }

    fun getStringPref(context: Context, key: String): String = context.getSharedPreferences(
        ANYWHERE_GPT_PREFERENCE,
        Context.MODE_PRIVATE
    ).getString(key, NO_VALUE_PREF).toString()

    fun setIntegerPref(context: Context, key: String, value: Int) {
        context.getSharedPreferences(ANYWHERE_GPT_PREFERENCE, Context.MODE_PRIVATE).edit()
            .putInt(key, value)
            .apply()
    }

    fun getIntegerPref(context: Context, key: String): Int {
        return context.getSharedPreferences(ANYWHERE_GPT_PREFERENCE, Context.MODE_PRIVATE)
            .getInt(key, NO_VALUE)
    }

    fun setLongPref(context: Context, key: String, value: Long) {
        context.getSharedPreferences(ANYWHERE_GPT_PREFERENCE, Context.MODE_PRIVATE).edit()
            .putLong(key, value)
            .apply()
    }

    fun getLongPref(context: Context, key: String): Long =
        context.getSharedPreferences(ANYWHERE_GPT_PREFERENCE, Context.MODE_PRIVATE)
            .getLong(key, NO_VALUE_LONG)

    fun setBooleanPref(context: Context, key: String, value: Boolean) {
        context.getSharedPreferences(ANYWHERE_GPT_PREFERENCE, Context.MODE_PRIVATE).edit()
            .putBoolean(key, value)
            .apply()
    }

    fun getBooleanPref(context: Context, key: String): Boolean =
        context.getSharedPreferences(ANYWHERE_GPT_PREFERENCE, Context.MODE_PRIVATE)
            .getBoolean(key, false)

    fun <T> setObjectPref(context: Context, key: String, value: T) {
        val gson = Gson()
        val json = gson.toJson(value)
        setStringPref(context, key, json)
    }

    fun <T> getObjectPref(context: Context, key: String, objClass: Class<T>): T? {
        val gson = Gson()
        val json: String = getStringPref(context, key)
        return gson.fromJson(json, objClass)
    }

    fun clearAllPref(context: Context): Boolean =
        context.getSharedPreferences(ANYWHERE_GPT_PREFERENCE, Context.MODE_PRIVATE).edit()
            .clear()
            .commit()

    fun clearPref(context: Context, key: String): Boolean =
        context.getSharedPreferences(ANYWHERE_GPT_PREFERENCE, Context.MODE_PRIVATE).edit()
            .remove(key)
            .commit()

// </editor-fold>
}
