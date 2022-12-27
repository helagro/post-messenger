package se.helagro.postmessenger.network

import se.helagro.postmessenger.postitem.PostItem
import se.helagro.postmessenger.postitem.PostItemStatus
import se.helagro.postmessenger.settings.SettingsValues
import se.helagro.postmessenger.settings.preferenceInfo
import se.helagro.postmessenger.settings.StorageHandler
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread


class NetworkHandler(private val endpoint: String) {

    companion object {
        private const val REQUEST_METHOD = "POST"
        private const val CONNECT_TIMEOUT = 7000 // in milliseconds
        private const val ERROR_CODE = -1

        fun getEndpoint(): String? {
            val storageHandler = StorageHandler.getInstance()
            return SettingsValues.getInstance().endPoint
        }
    }

    private val jsonKey = SettingsValues.getInstance().jsonKey

    fun sendMessage(postItem: PostItem, listener: NetworkHandlerListener) {
        thread {
            val responseCode = makeRequest(postItem.msg)

            if (responseCode == 200) postItem.status = PostItemStatus.SUCCESS
            else postItem.status = PostItemStatus.FAILURE

            listener.onPostItemUpdate(responseCode)
        }
    }

    private fun makeRequest(msg: String): Int {
        var connection: HttpURLConnection? = null
        var reader: BufferedReader? = null
        var resCode: Int
        val data = "&" + jsonKey + "=" + URLEncoder.encode(msg, "UTF-8")

        try {
            connection = URL(this.endpoint).openConnection() as HttpURLConnection
            connection.connectTimeout = CONNECT_TIMEOUT
            connection.requestMethod = REQUEST_METHOD
            connection.doOutput = true

            val writer = OutputStreamWriter(connection.outputStream)
            writer.write(data)
            writer.flush()

            reader =
                BufferedReader(InputStreamReader(connection.inputStream)) //nothing works without this!
            resCode = connection.responseCode
        } catch (_: Exception) {
            resCode = ERROR_CODE
        }

        // ========== CLOSING ==========
        try {
            reader?.close()
            connection?.disconnect()
        } catch (_: Exception) { }

        return resCode
    }

}