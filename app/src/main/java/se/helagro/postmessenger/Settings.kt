package se.helagro.postmessenger

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.settings.*


private const val TAG = "Settings"

class Settings: AppCompatActivity() {
    companion object{
        const val ENDPOINT_PREFERENCE_ID = "endpoint_preference"
    }

    private val storageHandler = StorageHandler.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val actionBar: ActionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
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
        storageHandler.setString(ENDPOINT_PREFERENCE_ID, endpointInput.text.toString())
    }
}