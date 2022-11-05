package se.helagro.postmessenger

class PostItems : ArrayList<PostItem>{
    constructor()

    private val listeners = ArrayList<PostItemsListener>()

    fun addListener(listener: PostItemsListener){
        listeners.add(listener)
    }

    fun removeListener(listener: PostItemsListener){
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