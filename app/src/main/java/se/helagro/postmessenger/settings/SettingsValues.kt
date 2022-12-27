package se.helagro.postmessenger.settings

class SettingsValues {
    private val storageHandler = StorageHandler.getInstance()

    var endPoint = storageHandler.getString(preferenceInfo.ENDPOINT)
    var jsonKey = storageHandler.getString(preferenceInfo.JSON_KEY)

    fun save(){
        storageHandler.setString(preferenceInfo.ENDPOINT, endPoint)
        storageHandler.setString(preferenceInfo.JSON_KEY, jsonKey)
    }
}