package se.helagro.postmessenger.network

import android.os.Handler
import android.os.Looper
import se.helagro.postmessenger.postitem.PostItem
import se.helagro.postmessenger.postitem.PostItemStatus
import se.helagro.postmessenger.settings.DefaultSettingsValues
import se.helagro.postmessenger.settings.SettingsID
import se.helagro.postmessenger.settings.StorageHandler
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
            return storageHandler.getString(SettingsID.ENDPOINT)
        }
    }

    private val client: HttpURLConnection
    private val jsonKey: String

    init {
        val url = URL(endpoint)
        client = url.openConnection() as HttpURLConnection
        jsonKey = StorageHandler.getInstance().getString(SettingsID.JSON_KEY) ?: DefaultSettingsValues.JSON_KEY.value
    }

    fun sendMessage(postItem: PostItem, listener: NetworkHandlerListener) {
        thread{
            val responseCode = makeRequest(postItem.msg)

            if(responseCode == 200) postItem.status = PostItemStatus.SUCCESS
            else postItem.status = PostItemStatus.FAILURE

            listener.onPostItemUpdate(responseCode)
        }
    }

    private fun makeRequest(msg: String): Int {
        var connection: HttpURLConnection? = null
        var reader: BufferedReader? = null
        val data = "&" + jsonKey + "=" + URLEncoder.encode(msg, "UTF-8")

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
                reader?.close()
                connection?.disconnect()
            } catch (e: Exception) {
                return -1
            }
        }
    }

}