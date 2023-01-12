package se.helagro.postmessenger.network

interface NetworkRequestListener {
    fun onNetworkPostUpdate(resCode: Int?, message: String?)
}