package se.helagro.postmessenger

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import se.helagro.postmessenger.network.NetworkHandlerListener
import se.helagro.postmessenger.posthistory.PostHistory
import se.helagro.postmessenger.posthistory.gui.PostHistoryListAdapter
import se.helagro.postmessenger.settings.gui.SettingsActivity
import se.helagro.postmessenger.settings.SettingsValues

class MainActivity : AppCompatActivity(), NetworkHandlerListener {
    private val postHistory = PostHistory.getInstance()


    //=========== ENTRY POINTS ===========

    private val settingsLauncher = registerForActivityResult(StartActivityForResult()) {
        attemptSetup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        attemptSetup()
    }



    private fun attemptSetup() {
        if (SettingsValues.getInstance().areSettingsValid()) {
            setupViews()
        } else {
            goToSettings()
        }
    }

    private fun goToSettings() {
        settingsLauncher.launch(Intent(this, SettingsActivity::class.java))
    }


    //========== VIEW SETUP ==========

    private fun setupViews() {
        //INPUT_FIELD
        focusOnInputField()
        val inputFieldListener = InputFieldListener(this)
        inputField.setOnEditorActionListener(inputFieldListener)

        //POST_LIST
        val postListAdapter = PostHistoryListAdapter(this, postHistory, this)
        postLogList.adapter = postListAdapter
        postHistory.addListener(postListAdapter)
    }

    private fun focusOnInputField() {
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


    // ========== POST ITEM UPDATE ==========

    override fun onPostItemUpdate(code: Int?, message: String?) {
        this.runOnUiThread{
            postHistory.alertListeners()

            if (code != null && message != null) {
                showErrorDialog(code, message)
            }
        }
    }

    fun showErrorDialog(code: Int, message: String){
        AlertDialog.Builder(this)
            .setTitle(code.toString())
            .setMessage(message)
            .setNeutralButton(
                "Ok",
                DialogInterface.OnClickListener { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                })
            .show()
    }


    // ========== ENDING LIFECYCLE ==========

    override fun onDestroy() {
        super.onDestroy()

        val listAdapter = postLogList.adapter as PostHistoryListAdapter?
        if(listAdapter != null){
            postHistory.removeListener(listAdapter)
        }
    }
}