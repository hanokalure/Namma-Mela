package com.nammamela.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NammaMelaApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
