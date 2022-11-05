package se.helagro.postmessenger

import android.app.Application
import android.util.Log

private const val TAG = "MyApp"

class MyApp : Application() {
    override fun onCreate() {
        Log.d(TAG, "Application started")

        super.onCreate()
        StorageHandler.init(applicationContext)
        StorageHandler.getInstance().setString(Settings.ENDPOINT_PREFERENCE_ID, Env.ENDPOINT_URL)
    }
}