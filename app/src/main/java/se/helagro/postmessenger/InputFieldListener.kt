package se.helagro.postmessenger

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import se.helagro.postmessenger.network.NetworkHandler
import se.helagro.postmessenger.network.NetworkHandlerListener
import se.helagro.postmessenger.posthistory.PostHistory
import se.helagro.postmessenger.postitem.PostItem

class InputFieldListener(val networkHandler: NetworkHandler, val postHistory: PostHistory) :
    TextView.OnEditorActionListener, NetworkHandlerListener {

    override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
        if (!isUserDone(actionId)) return false
        if (p0 == null) return false

        val textInput = p0.text.toString()
        val postItem = PostItem(textInput)
        postHistory.add(postItem)

        networkHandler.sendMessage(postItem, this)
        p0.text = ""
        return true
    }

    fun isUserDone(actionId: Int): Boolean {
        return actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL
    }

    override fun onPostItemUpdate(code: Int) {
        postHistory.alertListeners()
    }
}