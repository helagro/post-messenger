package se.helagro.postmessenger
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    val postItems = PostItems()
    private lateinit var postHandler: PostHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPostHandler()

        setContentView(R.layout.activity_main)
        focusOnInputField()
        val inputFieldListener = InputFieldListener(postHandler, postItems)
        inputField.setOnEditorActionListener(inputFieldListener)

        val postListAdapter = PostListAdapter(this, postItems)
        postLogList.adapter = postListAdapter
        postItems.addListener(postListAdapter)
    }

    private fun initPostHandler(): Boolean{
        val postHandlerEndpoint = PostHandler.getEndpoint()
        if(postHandlerEndpoint == null){
            goToSettings()
            return false
        }

        postHandler = PostHandler(postHandlerEndpoint)
        return true
    }

    private fun goToSettings(){
        val intent = Intent(this, Settings::class.java)
        startActivity(intent)
    }

    private fun focusOnInputField(){
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(inputField, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onStop() {
        super.onStop()
        postItems.removeListener(postLogList.adapter as PostListAdapter)
    }
}