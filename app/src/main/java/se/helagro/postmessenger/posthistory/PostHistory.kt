package se.helagro.postmessenger.posthistory

import se.helagro.postmessenger.postitem.PostItem

class PostHistory private constructor() : ArrayList<PostItem>() {
    private val listeners = ArrayList<PostHistoryListener>()

    companion object {
        private var instance: PostHistory? = null
        private val SYNC_LOCK = Any()

        @Synchronized
        fun getInstance(): PostHistory{
            synchronized(SYNC_LOCK) {
                if(instance == null) instance = PostHistory()
                return instance!!
            }
        }
    }


    // ========== LISTENERS ==========

    fun addListener(listener: PostHistoryListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: PostHistoryListener) {
        listeners.remove(listener)
    }

    fun alertListeners() {
        for (listener in listeners) {
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