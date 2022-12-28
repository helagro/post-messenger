package se.helagro.postmessenger

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import se.helagro.postmessenger.network.NetworkMessenger
import se.helagro.postmessenger.network.NetworkHandlerListener
import se.helagro.postmessenger.posthistory.PostHistory
import se.helagro.postmessenger.postitem.PostItem

class InputFieldListener(
    private val networkHandlerListener: NetworkHandlerListener
) :
    TextView.OnEditorActionListener {

    val postHistory = PostHistory.getInstance()

    override fun onEditorAction(textView: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
        if (!isUserDone(actionId) || textView == null) return false

        val input = textView.text.toString()
        val newPostItem = PostItem(input)
        postHistory.add(newPostItem)

        NetworkMessenger.sendMessage(newPostItem, networkHandlerListener)

        textView.text = ""
        return true
    }

    private fun isUserDone(actionId: Int): Boolean {
        return actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL
    }
}