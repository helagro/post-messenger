package se.helagro.postmessenger

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView

class InputFieldListener : TextView.OnEditorActionListener{
    val postHandler: PostHandler
    val postLogItems: ArrayList<PostLogItem>

    constructor(postHandler: PostHandler, postLogItems: ArrayList<PostLogItem>){
        this.postHandler = postHandler
        this.postLogItems = postLogItems
    }

    override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
        if(actionId != EditorInfo.IME_ACTION_DONE) return false
        if(p0 == null) return false

        val textInput = p0.text.toString()
        val postLogItem = PostLogItem(textInput)
        postLogItems.add(postLogItem)

        postHandler.sendMessage(postLogItem)
        p0.text = ""
        return true
    }
}