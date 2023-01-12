package se.helagro.postmessenger.settings

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences


class StorageHandler {

    companion object {
        private const val PREFERENCES_NAME = "main_preferences"

        private var instance: StorageHandler? = null

        fun initialize(application: Application) {
            instance = StorageHandler(application)
        }

        fun getInstance(): StorageHandler {
            if (instance == null) throw Exception("StorageHandler has not been initialized")
            return instance!!
        }
    }

    private constructor(application: Application) {
        sharedPreferences =
            application.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE)
    }

    private val sharedPreferences: SharedPreferences

    fun getString(preferenceInfo: PreferenceInfo): String {
        val defaultVal = preferenceInfo.defaultVal as String
        return sharedPreferences.getString(preferenceInfo.id, defaultVal)!!
    }

    fun setString(id: PreferenceInfo, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(id.id, value)
        editor.apply()
    }
}