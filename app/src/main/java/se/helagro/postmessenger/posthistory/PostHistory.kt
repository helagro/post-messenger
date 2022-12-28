package se.helagro.postmessenger.posthistory

import se.helagro.postmessenger.postitem.PostItem

class PostHistory() : ArrayList<PostItem>() {

    private val listeners = ArrayList<PostHistoryListener>()


    // ========== LISTENERS ==========

    fun addListener(listener: PostHistoryListener){
        listeners.add(listener)
    }

    fun removeListener(listener: PostHistoryListener){
        listeners.remove(listener)
    }

    fun alertListeners(){
        for(listener in listeners){
            listener.onPostHistoryUpdate()
        }
    }


    // ========== OVERRIDES ==========

    override fun add(element: PostItem): Boolean {
        val result = super.add(element)
        alertListeners()
        return result
    }
}