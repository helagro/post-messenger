package se.helagro.postmessenger

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


//NOT THREADING SAFE!!
class StorageHandler {
    companion object{
        private const val PREFERENCES_NAME = "main_preferences"
        private var instance: StorageHandler? = null

        fun init(context: Context){
            instance = StorageHandler(context)
        }

        fun getInstance(): StorageHandler{
            if(instance == null) throw Exception("StorageHandler has not been initialized")
            return instance!!
        }
    }

    private val sharedPreferences: SharedPreferences

    private constructor(context: Context){
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE)
    }

    fun getString(id: String): String?{
        return sharedPreferences.getString(id, null)
    }


}