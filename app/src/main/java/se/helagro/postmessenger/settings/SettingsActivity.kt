package se.helagro.postmessenger.settings

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.settings.*
import se.helagro.postmessenger.R


class SettingsActivity : AppCompatActivity() {
    private val settingsValues = SettingsValues.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.settings)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        fillInputFields()

        doneBtn.setOnClickListener {
            saveSettings()
            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
        }
    }

    private fun fillInputFields() {
        endpointInput.setText(settingsValues.endPoint)
        jsonKeyInput.setText(settingsValues.jsonKey)
    }

    //return-button in actionBar onClick
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(hasUnsavedChanges()){
            showUnsavedChangesDialog()
        } else {
            finish()
        }

        return true
    }

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

    private fun saveSettings() {
        settingsValues.endPoint = endpointInput.text.toString()
        settingsValues.jsonKey = jsonKeyInput.text.toString()

        settingsValues.save()
    }
}