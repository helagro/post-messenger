package se.helagro.postmessenger

class PostHandler {
    companion object{
        private const val ENDPOINT_PREFERENCE_ID = "endpoint_preference"

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