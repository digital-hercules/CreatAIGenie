package com.ai.genie

import android.app.Application
import com.ai.genie.common.UsefullData

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Banuba Video Editor SDK
        val usefullData = UsefullData(this)
        usefullData.setTheme(this)
    }
}
