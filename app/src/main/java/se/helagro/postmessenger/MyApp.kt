package se.helagro.postmessenger

import android.app.Application
import android.util.Log

private const val TAG = "MyApp"

class MyApp : Application {
    constructor(){

    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Application started")
        StorageHandler.init(applicationContext)
    }
}