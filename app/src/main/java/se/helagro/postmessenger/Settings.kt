package se.helagro.postmessenger

import android.os.Bundle
import android.util.Log
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

        Log.d(TAG, "DId ")
    }

    override fun onPause() {
        saveSettings()

        super.onPause()
    }

    private fun saveSettings(){
        storageHandler.setString(ENDPOINT_PREFERENCE_ID, endpointInput.text.toString())
    }
}