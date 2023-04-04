package com.shashank.anywheregpt.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

// used for simple start activity without Intent parameters
fun Activity.goToActivity(newActivity: Class<*>) {
    val intent = Intent(this, newActivity)
    startActivity(intent)
}

// used for show a toast message in the UI Thread
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

// Transform simple object to String with Gson
inline fun <reified T : Any> T.toPrettyJson(): String = Gson().toJson(this, T::class.java)

// Transform String Json to Object
inline fun <reified T : Any> String.fromPrettyJson(): T = Gson().fromJson(this, T::class.java)

// Transform String List Json to Object
inline fun <reified T : Any> String.fromPrettyJsonList(): MutableList<T> =
    when (this.isNotEmpty()) {
        true -> Gson().fromJson(this, object : TypeToken<MutableList<T>>() {}.type)
        false -> mutableListOf()
    }

internal inline fun <reified T> Gson.getModelListFromJsonAsset(
    context: Context,
    fileName: String
): T {
    val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
    return json.fromPrettyJson()
}

/**
 * Use this to dismiss keyboards, can always wrap if you needed something else after dismissing
 */
fun Context.dismissKeyboard(view: View?) {
    view?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun <T> Fragment.getNavigationResult(key: String) =
    findNavController().currentBackStackEntry?.savedStateHandle?.get<T>(key)

fun <T> Fragment.getNavigationResultLiveData(key: String): MutableLiveData<T>? {
    viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            findNavController().previousBackStackEntry?.savedStateHandle?.remove<T>(key)
        }
    })

    return findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
}

fun <T> Fragment.setNavigationResult(key: String, result: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

@SuppressLint("HardwareIds")
fun Context.deviceId() = Settings.Secure.getString(
    contentResolver,
    Settings.Secure.ANDROID_ID
)

fun Context.manufacturer() = Build.MANUFACTURER

fun Context.deviceName() = "${Build.MANUFACTURER} ${Build.MODEL}"

fun View.show() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun Fragment.navigate(action: NavDirections) {
    findNavController().navigate(
        action
    )
}


inline fun <reified T> tryCatch(block: () -> T?): Result<T?> =
    try {
        block().let { Result.success(it) }
    } catch (e: ApiException) {
        Result.failure(e)
    } catch (e: NoInternetException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }

fun Fragment.addMenuProvider(@MenuRes menuRes: Int, callback: (id: MenuItem) -> Boolean) {
    val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(menuRes, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem) = callback(menuItem)

    }
    (requireActivity() as MenuHost).addMenuProvider(
        menuProvider,
        viewLifecycleOwner,
        Lifecycle.State.RESUMED
    )
}

fun Context.copyTextToClipboard(textToCopy: String) {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("text", textToCopy)
    clipboardManager.setPrimaryClip(clipData)
    Toast.makeText(this, "Text copied", Toast.LENGTH_LONG).show()
}