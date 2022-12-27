package se.helagro.postmessenger
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import se.helagro.postmessenger.network.NetworkHandler
import se.helagro.postmessenger.posthistory.PostHistory
import se.helagro.postmessenger.settings.SettingsActivity
import se.helagro.postmessenger.settings.SettingsValues

class MainActivity : AppCompatActivity() {
    private val postHistory = PostHistory()
    private var networkHandler: NetworkHandler? = null


    //=========== ENTRY POINTS ===========

    private val settingsLauncher = registerForActivityResult(StartActivityForResult()) {
        attemptSetup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        attemptSetup()
    }




    private fun attemptSetup(){
        if(SettingsValues.getInstance().areSettingsValid()){
            networkHandler = NetworkHandler()
            setupViews()
        } else {
            goToSettings()
        }
    }

    private fun goToSettings(){
        settingsLauncher.launch(Intent(this, SettingsActivity::class.java))
    }


    //========== VIEW SETUP ==========

    private fun setupViews(){
        //INPUT_FIELD
        focusOnInputField()
        val inputFieldListener = InputFieldListener(networkHandler!!, postHistory)
        inputField.setOnEditorActionListener(inputFieldListener)

        //POST_LIST
        val postListAdapter = PostHistoryListAdapter(this, postHistory)
        postLogList.adapter = postListAdapter
        postHistory.addListener(postListAdapter)
    }

    private fun focusOnInputField(){
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(inputField, InputMethodManager.SHOW_IMPLICIT)
    }


    //=========== MENU ===========

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        goToSettings()
        return true
    }



    override fun onDestroy() {
        super.onDestroy()
        (postLogList.adapter as PostHistoryListAdapter?)?.let { postHistory.removeListener(it) }
    }
}