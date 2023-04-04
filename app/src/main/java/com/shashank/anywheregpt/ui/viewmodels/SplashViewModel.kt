package com.shashank.anywheregpt.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    companion object {
        private const val DELAY_TIME_IN_MILLS = 3000L
    }

    private val _splashStateLiveData = MutableLiveData<SplashState>()
    val splashStateLiveData: LiveData<SplashState>
        get() = _splashStateLiveData

    init {
        GlobalScope.launch {
            delay(DELAY_TIME_IN_MILLS)
            _splashStateLiveData.postValue(SplashState.SplashScreen)
        }
    }
}

sealed class SplashState {
    object SplashScreen : SplashState()
}