package se.helagro.postmessenger

import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView

class InputFieldListener : TextView.OnEditorActionListener{
    private val postHandler = PostHandler()

    constructor(){
        postHandler.init()
    }

    override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
        if(actionId != EditorInfo.IME_ACTION_DONE) return false

        Log.d("daw", "grsgrs")
        return true
    }
}