package com.shashank.anywheregpt.utils

import android.content.Context
import android.content.Intent

object AppUtils {

    fun shareContent(context: Context, contentValue: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            contentValue
        )
        context.startActivity(Intent.createChooser(shareIntent, "Send to"))
    }
}