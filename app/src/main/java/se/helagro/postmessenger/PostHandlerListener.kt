package se.helagro.postmessenger

interface PostHandlerListener {
    fun onUpdate(code: Int)
}