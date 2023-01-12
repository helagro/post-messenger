package se.helagro.postmessenger.network

import android.util.Log
import se.helagro.postmessenger.postitem.PostItem
import se.helagro.postmessenger.postitem.PostItemStatus
import se.helagro.postmessenger.settings.SettingsValues
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread


object NetworkMessenger {
    private const val REQUEST_METHOD = "POST"
    private const val CONNECT_TIMEOUT = 15 * 1000
    private val ERROR_MESSAGES = hashMapOf<Int, String>(
        404 to "The resource at the specified URL does not exist"
    )

    fun sendMessage(postItem: PostItem, listener: NetworkRequestListener) {
        postItem.status = PostItemStatus.LOADING //to display right in views
        thread {
            val responseCode = makeRequest(postItem.msg)

            setPostItemStatus(responseCode, postItem)
            val errorMessage = ERROR_MESSAGES[responseCode]

            listener.onNetworkPostUpdate(responseCode, errorMessage)
        }
    }

    private fun makeRequest(msg: String): Int? {
        var connection: HttpURLConnection? = null
        try {
            connection = setupConnection()

            writeData(connection.outputStream, msg)
            readData(connection.inputStream)

            connection.disconnect()
        } catch (e: Exception) {
            Log.e("NetworkHandler", e.stackTraceToString())
        }

        return connection?.responseCode
    }

    private fun setupConnection(): HttpURLConnection {
        val endpoint = SettingsValues.getInstance().endpointRaw
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

    private fun setPostItemStatus(responseCode: Int?, postItem: PostItem){
        when (responseCode){
            200 ->
                postItem.status = PostItemStatus.SUCCESS
            else ->
                postItem.status = PostItemStatus.FAILURE
        }
    }
}