package se.helagro.postmessenger.network

import android.os.Handler
import android.os.Looper
import se.helagro.postmessenger.PostItem
import se.helagro.postmessenger.PostItemStatus
import se.helagro.postmessenger.Settings.Companion.ENDPOINT_PREFERENCE_ID
import se.helagro.postmessenger.StorageHandler
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread


class NetworkHandler(private val endpoint: String) {
    companion object{
        fun getEndpoint(): String?{
            val storageHandler = StorageHandler.getInstance()
            return storageHandler.getString(ENDPOINT_PREFERENCE_ID)
        }
    }

    private val client: HttpURLConnection

    init {
        val url = URL(endpoint)
        client = url.openConnection() as HttpURLConnection
    }

    fun sendMessage(postItem: PostItem, listener: NetworkHandlerListener) {
        val mainHandler = Handler(Looper.getMainLooper())
        thread{
            val code = makeRequest(postItem.msg)

            if(code == 200) postItem.status = PostItemStatus.SUCCESS
            else postItem.status = PostItemStatus.FAILURE
            mainHandler.post(thread {
                listener.onUpdate(code)
            })
        }
    }

    private fun makeRequest(msg: String): Int {
        var connection: HttpURLConnection? = null
        var reader: BufferedReader? = null
        val data = "&msg=" + URLEncoder.encode(msg, "UTF-8")

        try {
            connection = URL(this.endpoint).openConnection() as HttpURLConnection
            connection.connectTimeout = 7000
            connection.requestMethod = "POST"
            connection.doOutput = true

            val writer = OutputStreamWriter(connection.outputStream)
            writer.write(data)
            writer.flush()

            reader = BufferedReader(InputStreamReader(connection.inputStream)) //NOTHING WORKS WITHOUT THIS
            return connection.responseCode
        } catch (e: Exception) {
            return -1
        } finally {
            try {
                reader!!.close()
                connection?.disconnect()
            } catch (e: Exception) {
                return -1
            }
        }
    }

}