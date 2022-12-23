package se.helagro.postmessenger

class PostHistory : ArrayList<PostItem>{
    constructor()

    private val listeners = ArrayList<PostHistoryListener>()

    fun addListener(listener: PostHistoryListener){
        listeners.add(listener)
    }

    fun removeListener(listener: PostHistoryListener){
        listeners.remove(listener)
    }

    override fun add(element: PostItem): Boolean {
        val result = super.add(element)
        alertListeners()
        return result
    }

    fun alertListeners(){
        for(listener in listeners){
            listener.update()
        }
    }
}