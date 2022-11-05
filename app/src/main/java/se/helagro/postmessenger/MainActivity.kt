package se.helagro.postmessenger
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val settingsLauncher = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
        val didSucceed = initPostHandler()
        if(didSucceed){
            setupViews()
        }
    }
    val postItems = PostItems()
    private var postHandler: PostHandler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val didSucceed = initPostHandler()
        if(didSucceed){
            setupViews()
        }

    }

    private fun initPostHandler(): Boolean{
        val postHandlerEndpoint = PostHandler.getEndpoint()
        if(postHandlerEndpoint == null) {
            goToSettings()
            return false
        } else if(!URLUtil.isValidUrl(postHandlerEndpoint)){
            Toast.makeText(this, "Invalid endpoint URL", Toast.LENGTH_LONG).show()
            goToSettings()
            return false
        }

        postHandler = PostHandler(postHandlerEndpoint)
        return true
    }

    private fun goToSettings(){
        settingsLauncher.launch(Intent(this, Settings::class.java))
    }

    private fun setupViews(){
        //INPUT_FIELD
        focusOnInputField()
        val inputFieldListener = InputFieldListener(postHandler!!, postItems)
        inputField.setOnEditorActionListener(inputFieldListener)

        //POST_LIST
        val postListAdapter = PostListAdapter(this, postItems)
        postLogList.adapter = postListAdapter
        postItems.addListener(postListAdapter)
    }

    private fun focusOnInputField(){
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(inputField, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        goToSettings()
        return true
    }

    /**
     * ENDING LIFECYCLE
     */

    override fun onStop() {
        super.onStop()
        (postLogList.adapter as PostListAdapter?)?.let { postItems.removeListener(it) }
    }
}