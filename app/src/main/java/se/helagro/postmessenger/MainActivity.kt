package se.helagro.postmessenger
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var postHandler: PostHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPostHandler()

        setContentView(R.layout.activity_main)
        val inputFieldListener = InputFieldListener(postHandler)
        inputField.setOnEditorActionListener(inputFieldListener)
    }

    private fun initPostHandler(): Boolean{
        val postHandlerEndpoint = PostHandler.getEndpoint()
        if(postHandlerEndpoint == null){
            goToSettings()
            return false
        }

        postHandler = PostHandler(postHandlerEndpoint)
        return true
    }

    private fun goToSettings(){
        val intent = Intent(this, Settings::class.java)
        startActivity(intent)
    }

}