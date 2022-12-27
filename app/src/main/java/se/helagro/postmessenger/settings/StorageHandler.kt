package se.helagro.postmessenger.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application


//NOT THREADING SAFE
class StorageHandler private constructor(application: Application) {

    companion object{
        private const val PREFERENCES_NAME = "main_preferences"
        private var instance: StorageHandler? = null

        fun init(application: Application){
            instance = StorageHandler(application)
        }

        fun getInstance(): StorageHandler {
            if(instance == null) throw Exception("StorageHandler has not been initialized")
            return instance!!
        }
    }

    private val sharedPreferences = application.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE)

    fun getString(preferenceInfo: preferenceInfo): String{
        val defaultVal = preferenceInfo.defaultVal as String
        return sharedPreferences.getString(preferenceInfo.id, defaultVal)!!
    }

    @SuppressLint("ApplySharedPref")
    fun setString(id: preferenceInfo, value: String){
        val editor = sharedPreferences.edit()
        editor.putString(id.id, value)
        editor.commit()
    }
}