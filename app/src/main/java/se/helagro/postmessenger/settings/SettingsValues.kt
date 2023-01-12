package se.helagro.postmessenger.settings

class SettingsValues private constructor() {
    companion object {
        private val SYNC_LOCK = Any()
        private var instance: SettingsValues? = null

        @Synchronized
        fun getInstance(): SettingsValues{
            synchronized(SYNC_LOCK){
                if(instance == null) instance = SettingsValues()
                return instance!!
            }
        }
    }

    private val storageHandler = StorageHandler.getInstance()

    var endpointRaw = storageHandler.getString(PreferenceInfo.ENDPOINT)
    var jsonKey = storageHandler.getString(PreferenceInfo.JSON_KEY)

    fun areSettingsValid(): Boolean{
        return isEndpointValid()
    }

    fun isEndpointValid(): Boolean{
        return endpointRaw != ""
    }

    fun save(invalidSettingsListener: InvalidSettingsListener): Boolean{
        if(!areSettingsValid()) {
            invalidSettingsListener.onInvalidSettings()
            return false
        }

        storageHandler.setString(PreferenceInfo.ENDPOINT, endpointRaw)
        storageHandler.setString(PreferenceInfo.JSON_KEY, jsonKey)
        return true
    }
}