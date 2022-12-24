package se.helagro.postmessenger.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.settings.*
import se.helagro.postmessenger.R


class SettingsActivity: AppCompatActivity() {
    companion object{
    }

    private val storageHandler = StorageHandler.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val actionBar: ActionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)

        fillInputFields()

        doneBtn.setOnClickListener {
            saveSettings()
            finish()
        }
    }

    private fun fillInputFields(){
        endpointInput.setText(storageHandler.getString(SettingsID.ENDPOINT)?: "")
        jsonKeyInput.setText(storageHandler.getString(SettingsID.JSON_KEY)?: DefaultSettingsValues.JSON_KEY.value)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    override fun onPause() {
        saveSettings()
        super.onPause()
    }

    private fun saveSettings(){
        storageHandler.setString(SettingsID.ENDPOINT, endpointInput.text.toString())
    }
}