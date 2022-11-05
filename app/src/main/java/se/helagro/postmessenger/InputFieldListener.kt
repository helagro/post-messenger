package se.helagro.postmessenger

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView

class InputFieldListener : TextView.OnEditorActionListener, PostHandlerListener{
    val postHandler: PostHandler
    val postItems: PostItems

    constructor(postHandler: PostHandler, postItems: PostItems){
        this.postHandler = postHandler
        this.postItems = postItems
    }

    override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
        if(!(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL)) return false
        if(p0 == null) return false

        val textInput = p0.text.toString()
        val postItem = PostItem(textInput)
        postItems.add(postItem)

        postHandler.sendMessage(postItem, this)
        p0.text = ""
        return true
    }

    override fun onUpdate(code: Int) {
        postItems.alertListeners()
    }
}