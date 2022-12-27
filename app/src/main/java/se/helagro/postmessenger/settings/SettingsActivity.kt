package se.helagro.postmessenger.settings

import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.settings.*
import se.helagro.postmessenger.R


class SettingsActivity : AppCompatActivity(), InvalidSettingsListener {
    private val settingsValues = SettingsValues.getInstance()


    // ========== SETUP ==========

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.settings)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        fillInputFields()

        doneBtn.setOnClickListener {
            val success = saveSettings()
            if(success){
                Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun fillInputFields() {
        endpointInput.setText(settingsValues.endPoint)
        jsonKeyInput.setText(settingsValues.jsonKey)
    }



    // ========== LEAVING ACTIVITY ==========

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(hasUnsavedChanges()){
            showUnsavedChangesDialog()
        } else {
            finish()
        }
        return true
    }

    override fun onBackPressed() {
        if(hasUnsavedChanges()){
            showUnsavedChangesDialog()
        } else {
            super.onBackPressed()
        }
    }



    // ========== HANDLE UNSAVED CHANGES ==========

    private fun hasUnsavedChanges(): Boolean {
        return settingsValues.endPoint != endpointInput.text.toString()
                || settingsValues.jsonKey != jsonKeyInput.text.toString()
    }

    private fun showUnsavedChangesDialog(){
        AlertDialog.Builder(this)
            .setTitle("Do you want to save the changes?")
            .setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                saveSettings()
                finish()
            }
            .setNegativeButton("No") { _: DialogInterface, _: Int ->
                finish()
            }
    }



    // ========== SAVING ==========

    private fun saveSettings(): Boolean{
        settingsValues.endPoint = endpointInput.text.toString()
        settingsValues.jsonKey = jsonKeyInput.text.toString()

        return settingsValues.save(this)
    }

    override fun onInvalidSettings() {
        showInvalidSettingsMsg()
    }

    private fun showInvalidSettingsMsg(){
        var message = "Could not save settings"

        if(settingsValues.isEndpointValid()){
            message += ", endpoint url is invalid"
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}