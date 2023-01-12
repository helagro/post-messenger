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
import se.helagro.postmessenger.network.NetworkRequestListener
import se.helagro.postmessenger.posthistory.PostHistory
import se.helagro.postmessenger.posthistory.gui.PostHistoryListAdapter
import se.helagro.postmessenger.settings.gui.SettingsActivity
import se.helagro.postmessenger.settings.SettingsValues

class MainActivity : AppCompatActivity(), NetworkRequestListener {
    private val postHistory = PostHistory.getInstance()
    private lateinit var postHistoryListAdapter: PostHistoryListAdapter


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
        val settingsAreValid = SettingsValues.getInstance().areSettingsValid()

        if (settingsAreValid) {
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
        val inputFieldListener = InputFieldListener(this as NetworkRequestListener)
        inputField.setOnEditorActionListener(inputFieldListener)

        //POST_LIST
        postHistoryListAdapter = PostHistoryListAdapter(this, this as NetworkRequestListener)
        postHistoryListAdapter.subscribeToPostHistory()
        postLogList.adapter = postHistoryListAdapter
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

    override fun onNetworkRequestUpdate(resCode: Int?, message: String?) {
        this.runOnUiThread{
            postHistory.alertListeners()

            if (resCode != null && message != null) {
                showErrorDialog(resCode, message)
            }
        }
    }

    private fun showErrorDialog(code: Int, message: String){
        AlertDialog.Builder(this)
            .setTitle(code.toString())
            .setMessage(message)
            .setNeutralButton(
                "Ok"
            ) { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            }
            .show()
    }


    // ========== ENDING LIFECYCLE ==========

    override fun onDestroy() {
        super.onDestroy()

        postHistoryListAdapter.unsubscribeFromPostHistory()
    }
}