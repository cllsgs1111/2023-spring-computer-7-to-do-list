package com.example.todomanagement


import com.androidcourse.coursetable.App
import timber.log.Timber

class TodoManagementApplication : App() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.tag("成功")
        }
    }
}