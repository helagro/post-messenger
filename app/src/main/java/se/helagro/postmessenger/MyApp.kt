package se.helagro.postmessenger

import android.app.Application
import android.util.Log


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        StorageHandler.init(applicationContext)
    }
}