package se.helagro.postmessenger

import android.util.Log
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


    fun sendMessage(content: PostItem) {
        thread {
            httpPostRequest(this.endpoint, content.msg)
        }
    }

    fun httpPostRequest(url: String, msg: String?) {
        var conn: HttpURLConnection? = null
        var reader: BufferedReader? = null
        try {
            conn = URL(url).openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            val wr = OutputStreamWriter(conn.outputStream)
            val myData = "&msg=" + URLEncoder.encode(msg, "UTF-8")
            wr.write(myData)
            wr.flush()
            reader = BufferedReader(InputStreamReader(conn.inputStream)) //NOTHING WORKS WITHOUT THIS
        } catch (e: Exception) {
            Log.d("ö", e.toString())
        } finally {
            try {
                reader!!.close()
                conn?.disconnect()
            } catch (e: Exception) {
                Log.d("ö", e.toString())
            }
        }
    }
}