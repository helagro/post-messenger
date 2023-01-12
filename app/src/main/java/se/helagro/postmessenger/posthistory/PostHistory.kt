package se.helagro.postmessenger.posthistory

import android.util.Log
import se.helagro.postmessenger.postitem.PostItem

class PostHistory private constructor() {
    private val list = ArrayList<PostItem>()
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

    // ========== GETTERS ============

    fun getList(): ArrayList<PostItem>{
        return list
    }

    fun get(i: Int): PostItem{
        return list[i]
    }


    // ========== LISTENERS ==========

    fun addListener(listener: PostHistoryListener) {
        if(listeners.contains(listener)){
            Log.w("PostHistory", "Listener has already been added")
        } else {
            listeners.add(listener)
        }
    }

    fun removeListener(listener: PostHistoryListener) {
        listeners.remove(listener)
    }

    fun alertListeners() {
        for (listener in listeners) {
            listener.onPostHistoryUpdate()
        }
    }


    // ========== MODIFY LIST ==========

    fun add(element: PostItem): Boolean {
        val result = list.add(element)
        alertListeners()

        return result
    }
}