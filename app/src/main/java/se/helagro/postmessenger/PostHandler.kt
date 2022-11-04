package se.helagro.postmessenger

import se.helagro.postmessenger.Settings.Companion.ENDPOINT_PREFERENCE_ID

class PostHandler {
    companion object{


        fun getEndpoint(): String?{
            val storageHandler = StorageHandler.getInstance()
            return storageHandler.getString(ENDPOINT_PREFERENCE_ID)
        }
    }

    private val endpoint: String

    constructor(endpoint: String){
        this.endpoint = endpoint
    }

    fun sendMessage(content: String){
    }
}