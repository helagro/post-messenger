package se.helagro.postmessenger

class PostHandler {
    private var isInit = false

    fun init(){
        isInit = true
    }

    fun sendMessage(content: String){
        if(!isInit) throw Exception("PostHandler was not initialized")
    }
}