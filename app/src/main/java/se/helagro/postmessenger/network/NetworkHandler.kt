package se.helagro.postmessenger.network

import se.helagro.postmessenger.postitem.PostItem
import se.helagro.postmessenger.postitem.PostItemStatus
import se.helagro.postmessenger.settings.SettingsValues
import se.helagro.postmessenger.settings.StorageHandler
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread


object NetworkHandler {
    private const val REQUEST_METHOD = "POST"
    private const val CONNECT_TIMEOUT = 7000 // in milliseconds
    private const val ERROR_CODE = -1

    fun sendMessage(postItem: PostItem, listener: NetworkHandlerListener) {
        thread {
            val responseCode = makeRequest(postItem.msg)

            if (responseCode == 200) postItem.status = PostItemStatus.SUCCESS
            else postItem.status = PostItemStatus.FAILURE

            listener.onPostItemUpdate(responseCode)
        }
    }

    private fun makeRequest(msg: String): Int {
        return try {
            val connection = setupConnection()

            writeData(connection.outputStream, msg)
            readData(connection.inputStream)

            connection.disconnect()

            connection.responseCode //could be problematic - is closed
        } catch (_: Exception) {
            ERROR_CODE
        }
    }

    private fun setupConnection(): HttpURLConnection {
        val endpoint = SettingsValues.getInstance().endPoint
        val connection = URL(endpoint).openConnection() as HttpURLConnection
        connection.connectTimeout = CONNECT_TIMEOUT
        connection.requestMethod = REQUEST_METHOD
        connection.doOutput = true
        return connection
    }

    private fun writeData(stream: OutputStream, msg: String){
        val jsonKey = SettingsValues.getInstance().jsonKey
        val data = "&" + jsonKey + "=" + URLEncoder.encode(msg, "UTF-8")

        val writer = OutputStreamWriter(stream)
        writer.write(data)
        writer.flush()
        writer.close()
    }

    //don't know why this is needed, but it really is
    private fun readData(stream: InputStream){
        val reader =
            BufferedReader(InputStreamReader(stream))
        reader.close()
    }
}