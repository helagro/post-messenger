package se.helagro.postmessenger.settings

import android.webkit.URLUtil

class SettingsValues private constructor() {
    companion object {
        private var instance: SettingsValues? = null

        fun getInstance(): SettingsValues{
            if(instance == null) instance = SettingsValues()
            return instance!!
        }
    }

    private val storageHandler = StorageHandler.getInstance()

    var endPoint = storageHandler.getString(PreferenceInfo.ENDPOINT)
    var jsonKey = storageHandler.getString(PreferenceInfo.JSON_KEY)


    fun areSettingsValid(): Boolean{
        return isEndpointValid()
    }

    fun isEndpointValid(): Boolean{
        return URLUtil.isValidUrl(endPoint)
    }

    fun save(invalidSettingsListener: InvalidSettingsListener): Boolean{
        if(!areSettingsValid()) {
            invalidSettingsListener.onInvalidSettings()
            return false
        }

        storageHandler.setString(PreferenceInfo.ENDPOINT, endPoint)
        storageHandler.setString(PreferenceInfo.JSON_KEY, jsonKey)
        return true
    }
}