package se.helagro.postmessenger

import android.os.Handler
import android.os.Looper
import se.helagro.postmessenger.Settings.Companion.ENDPOINT_PREFERENCE_ID
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread


class PostHandler {
    companion object{
        fun getEndpoint(): String?{
            val storageHandler = StorageHandler.getInstance()
            return storageHandler.getString(ENDPOINT_PREFERENCE_ID)
        }
    }

    private val endpoint: String
    private val client: HttpURLConnection

    constructor(endpoint: String){
        this.endpoint = endpoint
        val url = URL(endpoint)
        client = url.openConnection() as HttpURLConnection
    }


    fun sendMessage(postItem: PostItem, listener: PostHandlerListener) {
        val mainHandler = Handler(Looper.getMainLooper())
        thread{
            val code = makeResquest(postItem.msg)

            if(code == 200) postItem.status = PostItemStatus.SUCCESS
            mainHandler.post(thread {
                listener.onUpdate(code)
            })
        }
    }

    fun makeResquest(msg: String): Int {
        var conn: HttpURLConnection? = null
        var reader: BufferedReader? = null
        try {
            conn = URL(this.endpoint).openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            val wr = OutputStreamWriter(conn.outputStream)
            val myData = "&msg=" + URLEncoder.encode(msg, "UTF-8")
            wr.write(myData)
            wr.flush()
            reader = BufferedReader(InputStreamReader(conn.inputStream)) //NOTHING WORKS WITHOUT THIS
            return conn.responseCode
        } catch (e: Exception) {
            return -1
        } finally {
            try {
                reader!!.close()
                conn?.disconnect()
            } catch (e: Exception) {
                return -1
            }
        }
    }

}