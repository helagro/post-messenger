package se.helagro.postmessenger

import android.app.Application
import se.helagro.postmessenger.settings.StorageHandler


class MyApp : Application() {
    override fun onCreate() {
        StorageHandler.init(this)
        super.onCreate()
    }
}