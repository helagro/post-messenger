package se.helagro.postmessenger

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


    fun sendMessage(content: String) {
        val url = URL(this.endpoint)
        val urlConnection = url.openConnection() as HttpURLConnection
        thread {
            httpPostRequest(this.endpoint, content)
        }

    }

    fun httpPostRequest( url: String, email: String?) {
        var response = ""
        var reader: BufferedReader? = null
        var conn: HttpURLConnection? = null
        try {
            val urlObj = URL(url)
            conn = urlObj.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            val wr = OutputStreamWriter(conn.outputStream)
            val mydata = ("&" + URLEncoder.encode("msg", "UTF-8").toString() + "="
                    + URLEncoder.encode(email, "UTF-8"))
            wr.write(mydata)
            wr.flush()
            reader = BufferedReader(InputStreamReader(conn.inputStream))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                reader!!.close()
                conn?.disconnect()
            } catch (e: Exception) { }
        }
    }
}