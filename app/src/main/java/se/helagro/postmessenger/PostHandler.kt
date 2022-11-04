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


    fun sendMessage(content: String) {
        val url = URL(this.endpoint)
        val urlConnection = url.openConnection() as HttpURLConnection
        thread {
            httpPostRequest(this.endpoint, "please")
        }

    }

    fun httpPostRequest( url: String, email: String?): String? {
        var response = ""
        var reader: BufferedReader? = null
        var conn: HttpURLConnection? = null
        try {
            Log.d("RequestManager", "$url ")
            val urlObj = URL(url)
            conn = urlObj.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn!!.doOutput = true
            val wr = OutputStreamWriter(conn.outputStream)
            val mydata = ("&" + URLEncoder.encode("Email", "UTF-8").toString() + "="
                    + URLEncoder.encode(email, "UTF-8"))
            wr.write(mydata)
            wr.flush()
            Log.d("post response code", conn.responseCode.toString() + " ")
            val responseCode = conn.responseCode
            reader = BufferedReader(InputStreamReader(conn.inputStream))
            val sb = StringBuilder()
            var line: String? = null
            while (reader.readLine().also { line = it } != null) {
                sb.append(
                    """
                    $line
                    
                    """.trimIndent()
                )
            }
            response = sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Error", "error")
        } finally {
            try {
                reader!!.close()
                conn?.disconnect()
            } catch (ex: Exception) {
            }
        }
        Log.d("RESPONSE POST", response)
        return response
    }
}