package se.helagro.postmessenger.network

interface NetworkRequestListener {
    fun onNetworkRequestUpdate(resCode: Int?, message: String?)
}