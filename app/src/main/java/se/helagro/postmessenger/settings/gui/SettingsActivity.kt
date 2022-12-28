package se.helagro.postmessenger.settings

import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlinx.android.synthetic.main.settings.*
import se.helagro.postmessenger.R


class SettingsActivity : AppCompatActivity(), InvalidSettingsListener {
    private val settingsValues = SettingsValues.getInstance()

    private lateinit var jsonKeyEditText: EditText
    private lateinit var endpointEditText: EditText


    // ========== SETUP ==========

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ViewDataBinding>(this, R.layout.settings)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initializeInputs()

        doneBtn.setOnClickListener {
            val success = saveSettings()
            if(success){
                Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initializeInputs(){
        jsonKeyEditText = jsonKeyInput as EditText
        endpointEditText = endpointInput as EditText

        endpointEditText.setText(settingsValues.endpointRaw)
        jsonKeyEditText.setText(settingsValues.jsonKey)
    }



    // ========== LEAVING ACTIVITY ==========

    //only used by back btn
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val canLeave = handleLeaving()
        if(canLeave) finish()
        return true
    }

    override fun onBackPressed() {
        val canLeave = handleLeaving()
        if(canLeave) super.onBackPressed()
    }

    private fun handleLeaving(): Boolean{
        if(hasUnsavedChanges()){
            showUnsavedChangesDialog()
            return false
        }
        if(!settingsValues.areSettingsValid()){
            showInvalidSettingsMsg()
            return false
        }

        return true
    }



    // ========== HANDLE UNSAVED CHANGES ==========

    private fun hasUnsavedChanges(): Boolean {
        return settingsValues.endpointRaw != endpointEditText.text.toString()
                || settingsValues.jsonKey != jsonKeyEditText.text.toString()
    }

    private fun showUnsavedChangesDialog(){
        AlertDialog.Builder(this)
            .setTitle("Do you want to save the changes?")
            .setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                saveSettings()
                val canLeave = handleLeaving()
                if(canLeave) finish()
            }
            .setNegativeButton("No") { _: DialogInterface, _: Int ->
                finish()
            }
            .show()
    }



    // ========== SAVING ==========

    private fun saveSettings(): Boolean{
        settingsValues.endpointRaw = endpointEditText.text.toString()
        settingsValues.jsonKey = jsonKeyEditText.text.toString()

        return settingsValues.save(this)
    }

    override fun onInvalidSettings() {
        showInvalidSettingsMsg()
    }

    private fun showInvalidSettingsMsg(){
        var message = ""

        if(!settingsValues.isEndpointValid()){
            message = "Endpoint URL is invalid"
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}