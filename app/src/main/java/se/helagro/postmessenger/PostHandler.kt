package se.helagro.postmessenger

import android.R.attr.data
import android.util.Log
import se.helagro.postmessenger.Settings.Companion.ENDPOINT_PREFERENCE_ID
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
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
            try {
                urlConnection.setRequestMethod("GET")
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "text/plain");
                urlConnection.setChunkedStreamingMode(0)
                val out: OutputStream = BufferedOutputStream(urlConnection.outputStream)

                out.write(content.toByteArray());
                out.flush();
                urlConnection.connect()
            } finally {
                urlConnection.disconnect()
            }
            Log.d("ö", "wafea" + content.toByteArray().toString())
        }

    }

    fun httpPostRequest(context: Context?, url: String, email: String?): String? {
        var response = ""
        var reader: BufferedReader? = null
        var conn: HttpURLConnection? = null
        try {
            Log.d("RequestManager", "$url ")
            Log.e("data::", " " + data)
            val urlObj = URL(url)
            conn = urlObj.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn!!.doOutput = true
            val wr = OutputStreamWriter(conn.outputStream)
            data += ("&" + URLEncoder.encode("Email", "UTF-8").toString() + "="
                    + URLEncoder.encode(email, "UTF-8"))
            wr.write(data)
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
                reader.close()
                conn?.disconnect()
            } catch (ex: Exception) {
            }
        }
        Log.d("RESPONSE POST", response)
        return response
    }
}