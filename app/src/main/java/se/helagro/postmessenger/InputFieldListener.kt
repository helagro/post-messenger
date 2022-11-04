package se.helagro.postmessenger

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView

class InputFieldListener : TextView.OnEditorActionListener{
    val postHandler: PostHandler

    constructor(postHandler: PostHandler){
        this.postHandler = postHandler
    }

    override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
        if(actionId != EditorInfo.IME_ACTION_DONE) return false
        if(p0 == null) return false

        postHandler.sendMessage(p0.text.toString())
        return true
    }
}