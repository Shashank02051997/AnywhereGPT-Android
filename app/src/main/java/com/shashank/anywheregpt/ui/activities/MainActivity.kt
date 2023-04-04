package com.shashank.anywheregpt.ui.activities

import android.os.Bundle
import com.shashank.anywheregpt.R
import com.shashank.anywheregpt.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}