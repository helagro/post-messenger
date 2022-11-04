package se.helagro.postmessenger
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var postHandler: PostHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val succeded = initPostHandler()
        Log.d(TAG, "dwafae" + succeded)
        if(!succeded) return

        setContentView(R.layout.activity_main)
        inputField.setOnEditorActionListener(InputFieldListener())
    }

    private fun initPostHandler(): Boolean{
        val postHandlerEndpoint = PostHandler.getEndpoint()
        if(postHandlerEndpoint == null){
            goToSettings()
            return false
        } else {
            postHandler = PostHandler(postHandlerEndpoint)
            return true
        }
    }

    private fun goToSettings(){
        val intent = Intent(this, Settings::class.java)
        startActivity(intent)
    }

}